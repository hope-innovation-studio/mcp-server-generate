<script setup>
import ToolSidebarItem from './ToolSidebarItem.vue'
defineProps({ activeTransport: String, tools: Array, searchText: String, loading: Boolean, errorMessage: String, activeTool: Object })
defineEmits(['update:transport', 'update:search-text', 'select-tool'])
const transports = ['HTTP', 'SSE', 'WS']
</script>
<template>
  <aside class="tool-sidebar glass-surface"><div class="sidebar-top"><p class="sidebar-title">TOOLS</p><div class="transport-tabs"><button v-for="transport in transports" :key="transport" class="transport-tab" :class="{ active: activeTransport === transport }" type="button" @click="$emit('update:transport', transport)">{{ transport }}</button></div><label class="search-box"><span>⌕</span><input :value="searchText" placeholder="搜索 Tool" @input="$emit('update:search-text', $event.target.value)"></label></div><div class="tool-list"><p v-if="loading" class="sidebar-message">正在加载 Tool…</p><p v-else-if="errorMessage" class="sidebar-message error-message">{{ errorMessage }}</p><div v-else-if="activeTransport !== 'HTTP'" class="empty-sidebar-state"><strong>{{ activeTransport }}</strong><span>暂未接入 Tool</span></div><template v-else-if="tools.length"><ToolSidebarItem v-for="tool in tools" :key="tool.id" :tool="tool" :active="activeTool?.id === tool.id" @click="$emit('select-tool', tool)" /></template><div v-else class="empty-sidebar-state"><strong>HTTP</strong><span>暂无可用 Tool</span></div></div></aside>
</template>
