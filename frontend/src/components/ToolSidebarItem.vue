<script setup>
defineProps({ tool: { type: Object, required: true }, active: Boolean })
defineEmits(['click'])
</script>
<template><button class="tool-sidebar-item" :class="{ active }" type="button" @click="$emit('click')"><span class="method-label" :class="`method-${(tool.requestMethod || 'http').toLowerCase()}`">{{ tool.requestMethod || 'HTTP' }}</span><span class="tool-name">{{ tool.name || '未命名 Tool' }}</span></button></template>
