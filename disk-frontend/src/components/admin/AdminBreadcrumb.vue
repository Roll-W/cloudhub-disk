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
  <n-breadcrumb>
    <n-breadcrumb-item @click="$router.push({name: adminIndex})">
        <n-dropdown :options="adminMenuOptions">
            <div>
                系统
            </div>
        </n-dropdown>
    </n-breadcrumb-item>
    <n-breadcrumb-item :clickable="false">
      <n-dropdown :options="menuOptions">
        <div>
          {{ menu.name }}
        </div>
      </n-dropdown>
    </n-breadcrumb-item>
    <n-breadcrumb-item>
      {{ current.name }}
    </n-breadcrumb-item>
  </n-breadcrumb>
</template>

<script setup>
import {adminIndex} from "@/router";
import {
    convertsToNMenuOptions, findMenuOptionByKey,
    keyAdmin,
    requestMenusByName
} from "@/views/menu";

const properties = defineProps({
  /**
   * Parent menu
   */
  menu: {
    type: String,
    required: true
  },
  /**
   * Current location
   */
  location: {
    type: String,
    required: true
  }
})

const adminMenu = findMenuOptionByKey(keyAdmin)

const menu = requestMenusByName(keyAdmin, properties.menu)
const current = menu.children.find(it => it.key === properties.location)
const children = [...menu.children]
const menuOptions = convertsToNMenuOptions(children)

const adminMenuOptions = convertsToNMenuOptions(adminMenu.menus)

</script>
