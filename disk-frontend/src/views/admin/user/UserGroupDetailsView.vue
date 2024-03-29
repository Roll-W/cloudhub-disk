<!--
  - Copyright (C) 2023 RollW
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -        http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<script setup>
import {getCurrentInstance, ref} from "vue";
import {useRouter} from "vue-router";
import {useNotification, useMessage, useDialog, NButton, NButtonGroup} from "naive-ui";
import api from "@/request/api";
import {createConfig} from "@/request/axios_config";
import {popAdminErrorTemplate} from "@/views/util/error";
import {adminUserDetails, adminUserGroupLists} from "@/router";
import {adminMenuUser} from "@/views/menu";
import AdminBreadcrumb from "@/components/admin/AdminBreadcrumb.vue";
import {formatFileSize, formatTimestamp} from "@/util/format";
import DisplayInput from "@/components/admin/DisplayInput.vue";
import UserGroupSettingForm from "@/views/admin/user/UserGroupSettingForm.vue";
import UserGroupMemberForm from "@/views/admin/user/UserGroupMemberForm.vue";

const router = useRouter()
const {proxy} = getCurrentInstance()
const notification = useNotification()
const message = useMessage()
const dialog = useDialog()

const userGroupId = router.currentRoute.value.params.id

const editSetting = ref({
    key: "",
    value: ""
})
const showEditSettingModal = ref(false)
const showAddUserModal = ref(false)

const members = ref([])
const userGroupInfo = ref({})

const modifiable = ref(parseInt(userGroupId) !== 0)

const settings = [
    {
        key: "group_file-number-limit",
        name: "文件数量限制",
        format: (value) => {
            if (parseInt(value) === -1) {
                return "无限制"
            }
            return value
        }
    },
    {
        key: "group_quota",
        name: "用户组配额",
        format: (value) => {
            return formatFileSize(value * 1024 * 1024, "无限制")
        }
    }
]

const transformSetting = (groupSettings) => {
    const result = []
    for (const setting of settings) {
        const settingValue = groupSettings
                .find(value => value.key === setting.key)
        if (!settingValue) {
            result.push({
                key: setting.key,
                name: setting.name,
                value: "未设置",
                rawValue: null
            })
            continue
        }
        if (setting.format) {
            result.push({
                key: setting.key,
                name: setting.name,
                value: setting.format(settingValue.value),
                rawValue: settingValue.value
            })
        } else {
            result.push({
                key: setting.key,
                name: setting.name,
                value: settingValue.value,
                rawValue: settingValue.value
            })
        }
    }
    return result
}

const formValue = ref({
    name: "",
    description: "",
})

const back = () => {
    router.push({
        name: adminUserGroupLists,
        query: {
            page: router.currentRoute.value.query.page,
        }
    })
}

const handleSettingEdit = (setting) => {
    editSetting.value = {
        key: setting.key,
        name: setting.name,
        value: setting.rawValue,
    }
    showEditSettingModal.value = true
}

const requestUserGroupInfo = () => {
    const config = createConfig()

    proxy.$axios.get(api.userGroups(true, userGroupId), config)
            .then((response) => {
                userGroupInfo.value = response.data
                formValue.value = {
                    name: userGroupInfo.value.name,
                    description: userGroupInfo.value.description,
                }
            })
            .catch((error) => {
                popAdminErrorTemplate(notification, error,
                        "获取用户组信息失败")
            })
}

const requestUserGroupMembers = () => {
    if (!modifiable.value) {
        return
    }

    const config = createConfig()

    proxy.$axios.get(api.userGroupsMembers(true, userGroupId), config)
            .then((response) => {
                members.value = response.data
            })
            .catch((error) => {
                popAdminErrorTemplate(notification, error,
                        "获取用户组成员失败")
            })
}

const handleViewUser = (member) => {
    router.push({
        name: adminUserDetails,
        params: {
            userId: member.userId
        },
        query: {
            refer: 'groupdetails',
            source: userGroupId
        }
    })
}

requestUserGroupInfo()
requestUserGroupMembers()

</script>

<template>
    <div class="p-5">
        <AdminBreadcrumb :location="adminUserGroupLists"
                         :menu="adminMenuUser"/>
        <div>
            <div class="flex items-baseline mt-5">
                <n-h1>
                    <span class="text-amber-400">
                        {{ userGroupInfo.name }}
                    </span>
                    用户组信息
                </n-h1>
                <div class="flex flex-grow justify-end">
                    <n-button @click="back">返回</n-button>
                </div>
            </div>
            <div class="grid grid-cols-10 py-3">
                <div class="text-gray-600 mr-3 ">
                    用户组信息
                </div>
                <div class="col-span-9">
                    <DisplayInput :value="userGroupInfo.id"
                                  name="用户组ID"/>
                    <DisplayInput v-model:value="userGroupInfo.name"
                                  :modify="modifiable"
                                  name="用户组名称"
                                  placeholder="输入用户组名称"/>
                    <DisplayInput v-model:value="formValue.description"
                                  :modify="modifiable"
                                  name="用户组描述"
                                  placeholder="输入用户组描述"/>
                    <DisplayInput :render="formatTimestamp"
                                  :value="userGroupInfo.createTime"
                                  name="用户组创建时间"/>
                    <DisplayInput :render="formatTimestamp"
                                  :value="userGroupInfo.updateTime"
                                  name="用户组更新时间"/>
                    <div class="flex">
                        <div class="flex-grow"></div>
                        <div class="py-5">
                            <n-button-group>
                                <n-button type="primary">保存</n-button>
                            </n-button-group>
                        </div>
                    </div>
                </div>
            </div>
            <div class="grid grid-cols-10 py-3">
                <div class="text-gray-600 mr-3 ">
                    用户组设置
                </div>
                <div class="col-span-9">
                    <div class="pb-3">
                        <n-alert type="info">
                            <div>
                                未设置的项目与默认用户组保持一致
                            </div>
                        </n-alert>
                    </div>
                    <n-table striped>
                        <thead>
                        <tr>
                            <th>设置项</th>
                            <th>设置值</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="setting in transformSetting(userGroupInfo.settings || [])"
                            :key="setting.key">
                            <td>{{ setting.name }}</td>
                            <td>{{ setting.value }}</td>
                            <td>
                                <n-button
                                        :disabled="!modifiable"
                                        secondary type="primary" @click="handleSettingEdit(setting)">
                                    修改
                                </n-button>
                            </td>
                        </tr>
                        </tbody>
                    </n-table>
                </div>
            </div>

            <div class="grid grid-cols-10 py-3">
                <div class="text-gray-600 mr-3 ">
                    用户组成员
                </div>
                <div class="col-span-9">
                    <div v-if="!modifiable">
                        <n-alert type="warning">
                            <div>
                                默认用户组默认包含所有未分配用户组的用户，此处不可修改
                            </div>
                        </n-alert>
                    </div>
                    <div v-else>
                        <n-table striped>
                            <thead>
                            <tr>
                                <th>用户ID</th>
                                <th>用户名</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="member in members" :key="member.id">
                                <td>{{ member.userId }}</td>
                                <td>{{ member.name }}</td>
                                <td>
                                    <n-button-group>
                                        <n-button secondary type="primary" @click="handleViewUser(member)">
                                            查看
                                        </n-button>
                                        <n-button secondary type="error">移除</n-button>
                                    </n-button-group>
                                </td>
                            </tr>
                            <tr>
                                <td class="text-center" colspan="3">
                                    <n-button secondary type="primary" @click="showAddUserModal = true">
                                        添加用户
                                    </n-button>
                                </td>
                            </tr>
                            </tbody>
                        </n-table>
                    </div>
                </div>
            </div>

            <div>
                <n-modal v-model:show="showEditSettingModal"
                         :show-icon="false"
                         closable
                         preset="dialog"
                         title="修改用户组设置"
                         transform-origin="center">
                    <UserGroupSettingForm
                            :group-id="userGroupId"
                            :name="editSetting.name"
                            :on-after-action="() => {
                                showEditSettingModal = false
                                requestUserGroupInfo()
                            }"
                            :on-click-cancel="() => showEditSettingModal = false"
                            :setting-key="editSetting.key"
                            :value="editSetting.value"
                    />
                </n-modal>
                <n-modal v-model:show="showAddUserModal"
                         :show-icon="false"
                         closable
                         preset="dialog"
                         title="添加用户组成员"
                         transform-origin="center">
                    <UserGroupMemberForm
                            :group-id="userGroupId"
                            :on-after-action="() =>{
                                showAddUserModal = false
                                requestUserGroupMembers()
                            }"
                            :on-click-cancel="() => showAddUserModal = false"
                    />
                </n-modal>
            </div>
        </div>
    </div>
</template>

<style scoped>

</style>