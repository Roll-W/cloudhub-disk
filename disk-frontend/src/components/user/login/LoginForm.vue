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
    <div class="flex flex-grow-1 flex-fill">
        <n-h2>
            <n-text type="primary">登录</n-text>
        </n-h2>
        <div class="flex flex-fill justify-end">
            <n-h3>
                <n-text type="info">尚未拥有账号？
                    <router-link #="{ navigate, href }" :to="{name: register}" custom>
                        <n-a :href="href" @click="navigate">
                            点此注册
                        </n-a>
                    </router-link>
                </n-text>
            </n-h3>
        </div>
    </div>
    <n-form ref="loginForm" :model="formValue" :rules="formRules">
        <n-form-item label="用户名/电子邮箱" path="identity">
            <n-input v-model:value="formValue.identity" placeholder="请输入用户名或电子邮箱"
                     @keydown.enter.prevent/>
        </n-form-item>
        <n-form-item label="密码" path="token">
            <n-input v-model:value="formValue.token" placeholder="请输入密码" show-password-on="click"
                     type="password"
                     @keydown.enter.prevent/>
        </n-form-item>
        <n-form-item path="rememberMe">
            <n-checkbox v-model:checked="formValue.rememberMe">
                记住我
            </n-checkbox>
        </n-form-item>
        <n-button-group class="w-full">
            <n-button class="w-full flex-grow-0" type="primary" @click="onLoginClick">
                登录
            </n-button>
            <n-button class="w-full" type="tertiary" @click="onResetClick">
                重置
            </n-button>
        </n-button-group>
        <div class="pt-3">
            <router-link #="{ navigate, href }" :to="{name: passwordResetPage}" custom>
                <n-a :href="href" @click="navigate">
                    忘记密码
                </n-a>
            </router-link>
        </div>

    </n-form>

</template>

<script setup>
import {getCurrentInstance, ref} from "vue";
import {useNotification} from "naive-ui";
import {useRouter} from "vue-router";
import {useUserStore} from "@/stores/user";
import {driveFilePage, passwordResetPage, register} from "@/router";
import {popUserErrorTemplate} from "@/views/util/error";
import api from "@/request/api";

const userStore = useUserStore()
const loginForm = ref(null)
const router = useRouter()
const notification = useNotification()
const {proxy} = getCurrentInstance()

const formValue = ref({
    identity: null,
    token: null,
    rememberMe: false
})

const formRules = {
    identity: [{
        required: true,
        validator(rule, value) {
            if (!value) {
                return new Error("需要填写用户名或电子邮箱地址")
            }
            return true
        },
        trigger: ['input']
    }],
    token: [{
        required: true,
        message: "请输入密码",
        trigger: ['input']
    }],
}

const validateFormValue = (callback) => {
    loginForm.value?.validate((errors) => {
        if (errors) {
            return
        }
        callback()
    });
}

const onLoginClick = (e) => {
    e.preventDefault()
    validateFormValue(() => {
        proxy.$axios.post(api.passwordLogin, formValue.value).then(res => {
            /**
             * @type {{ user: {
             * username: string, id: number,
             * nickname: string,
             * role: string, email: string },
             * token: string
             * }}
             */
            const recvData = res.data
            let user = {
                id: recvData.user.id,
                username: recvData.user.username,
                role: recvData.user.role,
            }
            userStore.loginUser(user, recvData.token, formValue.value.rememberMe)
            userStore.setUserData({
                avatar: null,
                nickname: recvData.user.nickname,
            })
            router.push({
                name: driveFilePage
            })
        }).catch(err => {
            const msg = err.tip
            popUserErrorTemplate(notification, {
                tip: msg,
                message: "登录错误"
            }, "登录错误", "登录错误")
        })
    })
}

const onResetClick = () => {
    formValue.value = {
        identity: null,
        token: null,
        rememberMe: false
    }
    loginForm.value?.restoreValidation()
}

const handleToRegister = () => {
    router.push({
        name: register
    })
}
</script>
