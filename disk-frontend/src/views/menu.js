import {h} from "vue";
import {RouterLink} from "vue-router";
import {
    adminIndex,
    driveFilePage, driveTagPage,
} from "@/router";

import {NIcon} from "naive-ui";
import FileIcon from "@/components/icon/FileIcon.vue";

export const keyUser = "user"
export const keyAdmin = "admin"

export const menuFile = "File"

const menuOptions = [
    {
        key: keyUser,
        menus: [
            {
                name: "文件",
                key: menuFile,
                children: [
                    {
                        name: "个人主页",
                        key: driveFilePage,
                        linked: true,
                        icon: () => {
                            return h(NIcon, {
                                class: "text-2xl"
                            }, {
                                default: () => h(FileIcon)
                            })
                        }
                    },
                    {
                        name: "标签",
                        key: driveTagPage,
                        linked: true
                    },
                    {
                        name: "组织文件",
                        key: "Organization",
                    }
                ]
            },
            {
                name: "收藏",
                key: "Fav",
            },
            {
                name: "分享",
                key: "Share",
            },
            {
                name: "回收站",
                key: "Recycle",
            },

        ]
    },
    {
        key: keyAdmin,
        menus: [
            {
                name: "系统首页",
                key: adminIndex,
                linked: true
            },
            {
                linked: true,
                name: "返回资料库",
                key: driveFilePage
            },
            {
                name: "用户管理",
                key: "user-management",
            },
            {
                name: "文件管理",
                key: "file-management",
                children: [
                    {
                        name: "文件列表",
                        key: "file-management-list"
                    },
                ]
            },
            {
                name: "标签管理",
                key: "file-tag",

            },
            {
                name: "可视化数据",
                key: "visual-data",
            },
            {
                name: "系统管理",
                key: 'menuSystem',
                children: [
                    {
                        name: "系统设置",
                        key: 'systemSettings'
                    },
                    {
                        name: "系统日志",
                        key: 'systemLog'
                    },
                    {
                        name: "集群监控",
                        key: 'system-cluster-monitor'
                    },
                    {
                        name: "系统监控",
                        key: "system-management-monitor"
                    },
                ]
            },

        ]
    }
]

export const findMenuOptionByKey = (key) => {
    return menuOptions.find(menuOption => menuOption.key === key)
}

export const requestChildrenMenus = (menuKey, key) => {
    return requestMenusByName(menuKey, key).children | []
}

export const requestMenusByName = (menuKey, key) => {
    const menuOption = findMenuOptionByKey(menuKey)
    if (!menuOption) {
        return []
    }
    return menuOption.menus.find(menu => menu.key === key) | {}
}

export const convertsToNMenuOptions = (menus) => {
    return menus.map(menu => {
        let children
        if (menu.children) {
            children = [...menu.children]
            children = convertsToNMenuOptions(children)
        } else {
            children = undefined
        }
        if (!menu.linked) {
            return {
                key: menu.key,
                label: menu.name,
                children: children,
                icon: menu.icon,
            }
        }
        return {
            key: menu.key,
            icon: menu.icon,
            label: () => h(
                RouterLink,
                {
                    to: {
                        name: menu.key,
                    }
                },
                {default: () => menu.name}
            ),
        }
    })
}
