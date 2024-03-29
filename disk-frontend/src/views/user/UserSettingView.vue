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

<template>
    <div class="p-5">
        <n-h1>个人设置</n-h1>
        <n-h2>
            基本信息
        </n-h2>
        <div>
            <n-form ref="userForm"
                    :model="userInfoFormValue"
                    :rules="userFormRules">
                <n-form-item label="用户名">
                    <n-text class="ml-1">
                        {{ (userInfo || {}).username }}
                    </n-text>
                </n-form-item>
                <n-form-item label="昵称（不填写昵称则默认使用用户名）"
                             path="nickname">
                    <n-input v-model:value="userInfoFormValue.nickname"
                             placeholder="输入昵称，不填写则默认使用用户名"
                             type="text"/>
                </n-form-item>
                <n-form-item label="邮箱"
                             path="email">
                    <n-input v-model:value="userInfoFormValue.email"
                             placeholder="请输入邮箱"
                             type="text"/>
                </n-form-item>
                <n-button-group>
                    <n-button type="primary" @click="handleUpdateUserInfo()">
                        更新信息
                    </n-button>
                    <n-button secondary type="default" @click="resetUserInfoForm">
                        重置
                    </n-button>
                </n-button-group>
            </n-form>
        </div>
        <n-divider/>
        <n-h2>
            用户数据
        </n-h2>
        <n-button secondary type="primary"
                  @click="$router.push({name: userStatsPage})">
            点击跳转查看
        </n-button>
        <n-divider/>
        <n-h2>
            账户设置与安全
        </n-h2>
        <n-grid cols="5" x-gap="20" y-gap="20">
            <n-grid-item span="2">
                <div class="pb-3">
                    <n-h3>
                        重置密码
                    </n-h3>
                    <n-text>
                        一个安全的密码可以使您的账户更安全，建议您定期更换密码。
                    </n-text>
                    <div class="pt-3">
                        <n-button secondary type="primary"
                                  @click="showResetPasswordModal = true">
                            重置密码
                        </n-button>
                    </div>
                </div>
                <n-divider/>
                <div>
                    <n-h3>
                        修改用户名
                    </n-h3>
                    <n-alert type="warning">
                        <n-text>
                            用户名不支持个人修改，如需修改请联系管理员。
                        </n-text>
                    </n-alert>
                </div>
                <n-divider/>
                <div>
                    <n-h3>
                        注销账户
                    </n-h3>
                    <div class="pt-3">
                        <n-button disabled tertiary type="error">
                            注销账户
                        </n-button>
                    </div>
                </div>
            </n-grid-item>
            <n-grid-item span="3">
                <div>
                    <n-h3>
                        登录日志
                    </n-h3>
                    <div>
                        <CurrentUserLoginLogs/>
                    </div>
                </div>
            </n-grid-item>
        </n-grid>
        <n-divider/>
        <div class="py-3">
            <n-h2>
                近期操作日志
            </n-h2>
            <CurrentUserOprationLogs/>
        </div>

        <n-modal v-model:show="showResetPasswordModal"
                 :show-icon="false"
                 preset="dialog"
                 title="重置密码"
                 transform-origin="center">
            <UserResetPasswordForm
                    :on-cancel="() => showResetPasswordModal = false"
                    :on-confirm="() => showResetPasswordModal = false"
            />
        </n-modal>

    </div>
</template>

<script setup>
import {useUserStore} from "@/stores/user";
import {getCurrentInstance, ref} from "vue";
import {useNotification, useMessage, useDialog} from "naive-ui";
import {createConfig} from "@/request/axios_config";
import api from "@/request/api";
import {popUserErrorTemplate} from "@/views/util/error";
import UserResetPasswordForm from "@/components/user/UserResetPasswordForm.vue";
import CurrentUserLoginLogs from "@/components/user/personal/CurrentUserLoginLogs.vue";
import CurrentUserOprationLogs from "@/components/user/personal/CurrentUserOprationLogs.vue";
import {userStatsPage} from "@/router";
import {useRouter} from "vue-router";

const router = useRouter()
const message = useMessage()
const dialog = useDialog()
const userStore = useUserStore()
const {proxy} = getCurrentInstance()
const notification = useNotification()

const showResetPasswordModal = ref(false)

const userInfoFormValue = ref({
    nickname: null,
    email: null,
})

const userInfo = ref()

const userForm = ref()
const userFormRules = {
    nickname: [
        {
            min: 1,
            max: 20,
            message: "昵称长度在1-20个字符之间",
            trigger: ["blur", "input"]
        },
    ],
    email: [
        {
            required: true,
            message: "请输入邮箱",
            trigger: ["blur", "input"]
        },
        {
            type: "email",
            message: "请输入正确的邮箱",
            trigger: ["blur", "input"]
        },
    ]
}

const resetUserInfoForm = () => {
    userInfoFormValue.value.nickname = userInfo.value.nickname
    userInfoFormValue.value.email = userInfo.value.email
}

const handleUpdateUserInfo = () => {
    userForm.value.validate().then(() => {
        const config = createConfig()
        const data = {
            nickname: userInfoFormValue.value.nickname,
            email: userInfoFormValue.value.email,
        }
        proxy.$axios.put(api.user(), data, config).then((res) => {
            message.success("更新用户信息成功")
            getUserInfo()
        }).catch((err) => {
            popUserErrorTemplate(notification, err, "更新用户信息失败")
        })
    })
}

const getUserInfo = () => {
    const config = createConfig()
    proxy.$axios.get(api.getCurrentUserInfo, config).then((res) => {
        console.log(res)
        userInfo.value = res.data
        userInfoFormValue.value.nickname = res.data.nickname
        userInfoFormValue.value.email = res.data.email

        userStore.setUserData({
            avatar: null,
            nickname: res.data.nickname,
        })
    }).catch((err) => {
        popUserErrorTemplate(notification, err, "获取用户信息失败")
    })
}


const init = () => {
    getUserInfo()
}

init()

</script>

<style scoped>

</style>