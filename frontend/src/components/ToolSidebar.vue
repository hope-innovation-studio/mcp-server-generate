<script setup>
import ToolSidebarItem from './ToolSidebarItem.vue'

defineProps({
  activeTransport: { type: String, required: true },
  tools: { type: Array, required: true },
  searchText: { type: String, required: true },
  loading: { type: Boolean, default: false },
  errorMessage: { type: String, default: '' },
  batchMode: { type: Boolean, default: false },
  selectedToolIds: { type: Set, required: true },
  activeTool: { type: Object, default: null },
})

defineEmits(['update:transport', 'update:search-text', 'select-tool'])

const transports = [
  { value: 'HTTP', label: 'HTTP' },
  { value: 'SSE', label: 'SSE' },
  { value: 'WS', label: 'WS' },
]
</script>

<template>
  <aside class="tool-sidebar glass-surface">
    <div class="sidebar-top">
      <p class="sidebar-title">TOOLS</p>
      <div class="transport-tabs" aria-label="通信方式">
        <button
          v-for="transport in transports"
          :key="transport.value"
          class="transport-tab"
          :class="{ active: activeTransport === transport.value }"
          type="button"
          @click="$emit('update:transport', transport.value)"
        >
          {{ transport.label }}
        </button>
      </div>

      <label class="search-box">
        <span>⌕</span>
        <input
          :value="searchText"
          type="search"
          placeholder="搜索 Tool"
          @input="$emit('update:search-text', $event.target.value)"
        >
      </label>
    </div>

    <div class="tool-list">
      <p v-if="loading" class="sidebar-message">正在扫描 Tool...</p>
      <p v-else-if="errorMessage" class="sidebar-message error-message">{{ errorMessage }}</p>

      <template v-else-if="activeTransport !== 'HTTP'">
        <div class="empty-sidebar-state">
          <strong>{{ activeTransport === 'SSE' ? 'SSE' : 'WebSocket' }}</strong>
          <span>暂未扫描到 Tool</span>
        </div>
      </template>

      <template v-else-if="tools.length">
        <ToolSidebarItem
          v-for="tool in tools"
          :key="`${tool.requestMethod}-${tool.endpoint}-${tool.name}`"
          :tool="tool"
          :batch-mode="batchMode"
          :selected="selectedToolIds.has(`${tool.requestMethod ?? 'HTTP'}:${tool.endpoint ?? ''}:${tool.name ?? ''}`)"
          :active="activeTool === tool"
          @click="$emit('select-tool', tool)"
        />
      </template>

      <div v-else class="empty-sidebar-state">
        <strong>HTTP</strong>
        <span>暂未扫描到 Tool</span>
      </div>
    </div>
  </aside>
</template>
