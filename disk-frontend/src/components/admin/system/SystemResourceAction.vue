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
    <n-popover
            placement="left"
            trigger="hover"
            @positive-click="handlePositiveClick"
    >
        <template #trigger>
            <div>
                <n-tag :bordered="false"
                       class="cursor-pointer"
                       style="cursor: pointer" type="primary">
                    {{ resourceKindName }}
                </n-tag>
            </div>
        </template>
        <template #default>
            <div class="">
                <n-grid :cols="2">
                    <n-grid-item span="2">
                        <n-grid :cols="2">
                            <n-grid-item span="1">
                                <n-text>资源ID</n-text>
                            </n-grid-item>
                            <n-grid-item class="pl-5" span="1">
                                <n-text>{{ resourceId }}</n-text>
                            </n-grid-item>
                        </n-grid>
                    </n-grid-item>
                    <n-grid-item span="2">
                        <n-grid :cols="2">
                            <n-grid-item span="1">
                                <n-text>资源类型</n-text>
                            </n-grid-item>
                            <n-grid-item class="pl-5" span="1">
                                <n-tag :bordered="false" type="primary">
                                    {{ resourceKindName }}
                                </n-tag>
                            </n-grid-item>
                        </n-grid>
                    </n-grid-item>
                </n-grid>
                <div class="pt-3 pb-2 ">
                    <n-button-group>
                        <n-button size="small" type="primary">查看</n-button>
                        <n-button secondary size="small">取消</n-button>
                    </n-button-group>
                </div>

            </div>
        </template>

    </n-popover>
</template>

<script setup>

import {useRouter} from "vue-router";
import {getSystemResourceKindName} from "@/views/names";

const router = useRouter()

const props = defineProps({
    resourceId: {
        type: Number,
        required: true
    },
    resourceKind: {
        type: String,
        required: true
    }
})

const resourceKindName = getSystemResourceKindName(props.resourceKind)

const handlePositiveClick = () => {

}

</script>

<style scoped>

</style>