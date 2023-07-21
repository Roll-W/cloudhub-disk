/*
 * Copyright (C) 2023 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import axios from 'axios'
import {useUserStore} from "@/stores/user";

axios.defaults.withCredentials = true

export function createConfig(isJsonBody = false) {
    const userStore = useUserStore()
    const config = {
        headers: {}
    }
    if (userStore.isLogin) {
        config.headers["Authorization"] = userStore.token
    }
    if (isJsonBody) {
        config.headers["Content-Type"] = "application/json"
    }
    return config
}

const tokenErrorCodes = [
    'A1001',
    'A1002'
]

export function createAxios(onLoginExpired = () => {
}) {
    const instance = axios.create({
        withCredentials: true,
    })
    instance.interceptors.response.use(
        response => {
            console.log(response)
            if (!(response.data || {}).errorCode) {
                return response
            }

            if (response.data.errorCode !== '00000') {
                return Promise.reject(response.data)
            }
            return response.data
        }, error => {
            console.log(error)
            if (axios.isCancel(error)) {
                return Promise.reject({
                    tip: '请求被取消',
                    message: '请求被取消',
                    errorCode: 'CANCEL',
                    status: 500
                })
            }

            if (!error.response) {
                return Promise.reject({
                    tip: '网络错误',
                    message: '网络错误',
                    errorCode: 'D0000',
                    status: 500
                })
            }

            if (isInTokenError(
                error.response.data.errorCode || '00000')) {
                onLoginExpired()
                return Promise.reject({
                    tip: '登录过期或登录凭据无效，请重新登录',
                    message: '登录过期或登录凭据无效，请重新登录',
                    errorCode: error.response.data.errorCode,
                    status: 401
                })
            }
            return Promise.reject(error.response.data)

        }
    )
    return instance
}

function isInTokenError(errorCode = '00000') {
    return tokenErrorCodes.includes(errorCode)
}

