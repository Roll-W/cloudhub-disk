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
    <div class="flex flex-fill flex-grow w-100 h-100vh h-full bg-no-repeat bg-cover bg-center bg">
        <div class="w-100 h-100 backdrop-blur-3xl">
            <div class="flex flex-col w-100 px-16 p-6 h-100">
                <div class="flex px-5 ">
                    <div class="rounded-2xl p-3
                    bg-neutral-200 bg-opacity-40">
                        <a>
                            <Logo/>
                        </a>
                    </div>
                </div>
                <div class="flex flex-col p-5 flex-1">
                    <div class="leading-tight sm:leading-tight text-4xl sm:text-6xl font-extrabold text-black ">
                        个人文件资料库
                    </div>
                    <div class="flex-fill py-7 text-[#4f4662] text-2xl leading-relaxed sm:w-2/5">
                        一个专业的个人文件资料库。可以快速、安全、便捷的管理您的个人文件资料。
                    </div>
                </div>

                <div class="flex flex-fill flex-col px-5">
                    <div class="flex flex-fill align-bottom">
                        <a v-if="!userStore.isLogin" class="link-btn" href="/user/login">
                            立即登录
                        </a>
                        <a v-else class="link-btn" href="/drive">
                            立即进入
                        </a>
                    </div>
                    <div class="text-[#4f4662] opacity-70
                    transition-all hover:text-black hover:opacity-100 hover:drop-shadow hover:shadow-white">
                        <a href="https://github.com/Roll-W" target="_blank">©2023 RollW. All rights reserved. 版权所有</a>
                        <br>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>

<script setup>
import {useUserStore} from "@/stores/user";
import {useRouter} from "vue-router";
import {driveFilePage} from "@/router";
import Logo from "@/components/icon/Logo.vue";

const userStore = useUserStore()

const router = useRouter()

const checkLogin = () => {
    if (userStore.isLogin) {
        router.push({
            name: driveFilePage
        })
    }
}

</script>

<style scoped>
.bg {
    background: radial-gradient(110vw 250vh ellipse at 0% 11%,
    #e7f0fc 46%,
    #e3d0fc 55%,
    #f82edd 65%,
    #ff3378 70%,
    #fa7931 75%,
    #ffffff 81%);
    background-size: 100% auto;
}

.link-btn {
    @apply text-xl transition-all bg-amber-500 hover:bg-amber-600
    focus:outline-none focus:ring-2 focus:ring-amber-400
    focus:ring-offset-2 focus:ring-offset-amber-50
    text-white font-semibold h-14 px-6 rounded-xl
    cursor-pointer
    w-full flex items-center justify-center sm:w-auto;
}

</style>