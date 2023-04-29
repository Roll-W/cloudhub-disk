<template>
    <div class="flex h-58 flex-col items-center p-6 cursor-pointer
             rounded-2xl transition-all duration-300
             ease-in-out w-[220px]
             hover:bg-gray-100 hover:bg-opacity-50 m-2"
         @mouseenter="fileMenuShowState = true"
         @mouseleave="fileMenuShowState = false"
    >
        <div :class="['w-100 flex justify-start transition-all duration-300 items-start align-baseline ',
          fileMenuShowState || checkedState ? 'opacity-100' : 'opacity-0']">
            <n-checkbox
                v-model:checked="checkedState"
                @update:checked="$emit('update:checked', checkedState)"/>
            <div class="pl-3 flex flex-fill justify-end">
                <n-button circle @click="onClickMoreOptions($event, file)">
                    <template #icon>
                        <n-icon size="20">
                            <MoreHorizonal20Regular/>
                        </n-icon>
                    </template>
                </n-button>
            </div>
        </div>

        <div class="px-5 pb-3">
            <n-icon v-if="file.type === 'folder' " size="80">
                <Folder24Regular/>
            </n-icon>
            <n-icon v-else size="80">
                <FileIcon/>
            </n-icon>
        </div>
        <div class="w-100 text-center truncate">
            <n-tooltip placement="bottom" trigger="hover">
                <template #trigger>
                    <div class="truncate select-none">
                        {{ file.name }}
                    </div>
                </template>
                {{ file.name }}
            </n-tooltip>
        </div>
        <div class="text-gray-400 select-none">
            {{ file.createdTime }}
        </div>
    </div>

</template>

<script>

</script>

<script setup>
import {ref} from 'vue'
import Folder24Regular from "@/components/icon/Folder24Regular.vue";
import FileIcon from "@/components/icon/FileIcon.vue";
import MoreHorizonal20Regular from "@/components/icon/MoreHorizonal20Regular.vue";
import {NIcon} from "naive-ui";


const props = defineProps({
    file: {
        type: Object,
        required: true
    },
    checked: {
        type: Boolean,
        default: false
    },
    onClickMoreOptions: {
        type: Function,
        default: (event, file) => {
        }
    },
})

const emits = defineEmits([
    'update:checked'
])


const fileMenuShowState = ref(false)
const checkedState = ref(props.checked)


</script>