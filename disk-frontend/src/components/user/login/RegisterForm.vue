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
            <n-text type="primary">注册</n-text>
        </n-h2>
        <div class="flex flex-fill justify-end">
            <n-h3>
                <n-a type="info" @click="handleToLogin">回到登录</n-a>
            </n-h3>
        </div>
    </div>
    <n-form ref="registerForm" :model="formValue" :rules="formRules">
        <n-form-item label="用户名" path="username">
            <n-input v-model:value="formValue.username" placeholder="请输入用户名"
                     @keydown.enter.prevent/>
        </n-form-item>
        <n-form-item label="邮箱" path="email">
            <n-input v-model:value="formValue.email" :input-props="{ type: 'email' }" placeholder="请输入邮箱"
                     type="text"
                     @keydown.enter.prevent/>
        </n-form-item>
        <n-form-item label="密码" path="password">
            <n-input v-model:value="formValue.password" placeholder="请输入密码" show-password-on="click"
                     type="password"
                     @keydown.enter.prevent/>
        </n-form-item>
        <n-form-item label="确认密码" path="confirmPassword">
            <n-input v-model:value="formValue.confirmPassword" placeholder="请再次输入密码" show-password-on="click"
                     type="password"
                     @keydown.enter.prevent/>
        </n-form-item>
        <n-form-item path="agree">
            <n-checkbox v-model:checked="formValue.agree">
                已阅读并同意
            </n-checkbox>
            <n-a @click="showUserContract = true">《用户协议》</n-a>
        </n-form-item>
        <n-button-group class="w-full">
            <n-button class="w-full flex-grow-0" type="primary" @click="onRegisterClick">
                注册
            </n-button>
            <n-button class="w-full" type="tertiary" @click="onResetClick">
                重置
            </n-button>
        </n-button-group>
    </n-form>

    <n-modal
            v-model:show="showUserContract"
            closable
            preset="card"
            size="huge"
            style="width: 65%;">
        <UserContract/>
    </n-modal>
</template>

<script setup>
import {getCurrentInstance, ref} from "vue";
import {useRouter} from "vue-router";
import api from "@/request/api.js";
import {useMessage} from "naive-ui";
import {login} from "@/router";
import UserContract from "@/components/user/UserContract.vue";

const router = useRouter();
const message = useMessage()
const {proxy} = getCurrentInstance()

const formValue = ref({
    username: null,
    email: null,
    password: null,
    confirmPassword: null,
    agree: false
});

const showUserContract = ref(false);

const registerForm = ref(null);

const formRules = ref({
    username: [
        {
            required: true,
            message: "请输入用户名",
            trigger: ["input"]
        },
        {
            min: 3,
            max: 20,
            message: "用户名长度在3-20之间",
            trigger: ['input', 'blur']
        },
        {
            pattern: /^[a-zA-Z_\-][\w\-]{3,20}$/,
            message: "用户名只能包含字母、数字、下划线和横线，且不能以数字开头",
            trigger: ['input', 'blur']
        }
    ],
    email: [
        {
            required: true,
            message: "请输入邮箱",
            trigger: ["input"]
        },
        {
            type: "email",
            message: "请输入正确的邮箱",
            trigger: ["input", "blur"]
        }
    ],
    password: [
        {
            required: true,
            message: "请输入密码",
            trigger: ["input"]
        },
        {
            min: 4, max: 20,
            message: '密码长度在 4 到 20 个字符',
            trigger: ['input', 'blur']
        },
        {
            pattern: /^[a-zA-Z0-9!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]{4,20}$/,
            message: '密码只能包含字母、数字和特殊字符，长度在 4 到 20 个字符',
            trigger: ['input', 'blur']
        }
    ],
    confirmPassword: [
        {
            required: true,
            message: "请再次输入密码",
            trigger: ["input"]
        },
        {
            validator(rule, value) {
                return value === formValue.value.password;
            },
            message: "两次输入密码不一致",
            trigger: ["input"]
        }
    ],
    agree: [
        {
            validator(rule, value) {
                return value === true;
            },
            message: "请阅读并同意用户协议",
            trigger: ["input"]
        }
    ]
});

const validateFormValue = (callback) => {
    registerForm.value?.validate((errors) => {
        if (errors) {
            return
        }
        callback()
    });
}

const onRegisterClick = () => {
    validateFormValue(() => {
        proxy.$axios.post(api.register, formValue.value)
                .then((response) => {
                    message.success('注册成功')
                    router.push({name: login})
                })
                .catch((error) => {
                    message.error(error.tip)
                })
    })
};

const handleToLogin = () => {
    router.push({
        name: login
    })
};

const onResetClick = () => {
    formValue.value = {
        username: null,
        email: null,
        password: null,
        confirmPassword: null,
        agree: false
    };
    registerForm.value?.restoreValidation()
};

</script>

<style scoped>

</style>