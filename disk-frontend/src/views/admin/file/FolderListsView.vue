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
import {getCurrentInstance, h, ref} from "vue";
import {useRouter} from "vue-router";
import {useNotification, useMessage, useDialog, NButtonGroup, NButton} from "naive-ui";
import {adminFolderLists, adminStorageDetails} from "@/router";
import {adminMenuFile} from "@/views/menu";
import AdminBreadcrumb from "@/components/admin/AdminBreadcrumb.vue";
import FastPagination from "@/components/FastPagination.vue";
import {formatTimestamp} from "@/util/format";
import {createConfig} from "@/request/axios_config";
import api from "@/request/api";
import {popAdminErrorTemplate} from "@/views/util/error";
import {usePage} from "@/views/util/pages";

const router = useRouter()
const {proxy} = getCurrentInstance()
const notification = useNotification()
const message = useMessage()
const dialog = useDialog()
const page = usePage()

const columns = [
    {
        title: "ID",
        key: "storageId",
        width: 50
    },
    {
        title: "从属用户",
        key: "ownerId",
        width: 90
    },
    {
        title: "名称",
        key: "name",
        ellipsis: {
            tooltip: true
        },
    },
    {
        title: "所属文件夹",
        key: "parentId",
        width: 80,
    },
    {
        title: "创建时间",
        key: "createTime",
        ellipsis: {
            tooltip: true
        },
        render: (row) => {
            return formatTimestamp(row.createTime)
        }
    },
    {
        title: "最后修改时间",
        key: "updateTime",
        ellipsis: {
            tooltip: true
        },
        render: (row) => {
            return formatTimestamp(row.updateTime)
        }
    },
    {
        title: "操作",
        key: "actions",
        render(row) {
            return h(
                    NButtonGroup,
                    {},
                    () => [
                        h(NButton,
                                {
                                    onClick: () => {
                                        router.push({
                                            name: adminStorageDetails,
                                            params: {
                                                id: row.storageId,
                                                type: 'folder',
                                                ownerId: row.ownerId,
                                                ownerType: row.ownerType.toLowerCase()
                                            }
                                        })
                                    }
                                },
                                {default: () => "查看/编辑"}),
                        h(NButton,
                                {
                                    onClick: () => {
                                    }
                                },
                                {default: () => "删除"}),
                    ]
            );
        }
    }
]
const data = ref()

const getFolderLists = () => {
    const config = createConfig()
    config.params = {
        page: page.value.page,
    }

    proxy.$axios.get(api.folders(true), config).then((res) => {
        page.value.count = Math.ceil(res.total / res.size)
        page.value.page = res.page
        data.value = res.data
    }).catch((error) => {
        popAdminErrorTemplate(notification, error)
    })
}

getFolderLists()

</script>

<template>
    <div class="p-5 ">
        <AdminBreadcrumb :location="adminFolderLists" :menu="adminMenuFile"/>
        <n-h1>文件夹列表</n-h1>
        <n-data-table
                :bordered="false"
                :columns="columns"
                :data="data"
                class="mt-5"
        />

        <FastPagination :page="page" :route-name="adminFolderLists"/>
    </div>
</template>

<style scoped>

</style>