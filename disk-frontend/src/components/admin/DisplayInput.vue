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
import {ref, watch} from "vue";

const props = defineProps({
    value: {
        type: [String, Number, Boolean],
        required: true
    },
    config: {
        type: Object,
        default: {}
    },
    name: {
        type: String,
        default: ''
    },
    modify: {
        type: Boolean,
        default: false
    },
    placeholder: {
        type: String,
        default: ''
    },
    type: {
        // type: text, switch, checkbox, select, date, image
        type: String,
        default: 'text'
    },
    key: {
        type: String,
        default: ''
    },
    render: {
        type: Function,
        default: null
    }
})

const inputValue = ref(props.value)

watch(() => props.value, (newValue) => {
    inputValue.value = newValue
})

const emit = defineEmits(['update:value'])

const config = (props.config || {}).name ? props.config : {
    key: props.key,
    name: props.name,
    modify: props.modify,
    type: props.type,
    placeholder: props.placeholder
}

const getFormattedValue = () => {
    if (props.config.render) {
        return props.config.render(props.value)
    }
    if (props.render) {
        return props.render(props.value)
    }

    return props.value
}

</script>

<template>
    <n-form-item :label="config.name" :path="config.key">
        <n-switch v-if="config.type === 'switch'"
                  v-model:value="inputValue"
                  size="large"
                  @update:value="emit('update:value', $event)"/>
        <n-checkbox v-else-if="config.type === 'checkbox'"
                    v-model:checked="inputValue"
                    size="large"
                    @update:checked="emit('update:value', $event)"/>
        <n-select v-else-if="config.type === 'select'"
                  v-model:value="inputValue"
                  :options="config.options"
                  size="large"
                  @update:value="emit('update:value', $event)"/>
        <n-date-picker v-else-if="config.type === 'date'"
                       v-model:value="inputValue"
                       size="large"
                       @update:value="emit('update:value', $event)"/>
        <n-image v-else-if="config.type === 'image'"
                 :src="inputValue"
                 size="large"
                 @update:value="emit('update:value', $event)"/>
        <n-input v-else-if="config.modify"
                 v-model:value="inputValue"
                 :placeholder="config.placeholder"
                 size="large"
                 @update:value="emit('update:value', $event)"/>
        <div v-else class="text-xl">
            {{ getFormattedValue() }}
        </div>
    </n-form-item>
</template>

<style scoped>

</style>