<script setup>
import { computed, onMounted, ref } from 'vue'
import ToolDetailWorkspace from './components/ToolDetailWorkspace.vue'
import ToolSidebar from './components/ToolSidebar.vue'
import BatchActionBar from './components/BatchActionBar.vue'

const activeTransport = ref('HTTP')
const searchText = ref('')
const isBatchMode = ref(false)
const selectedTool = ref(null)
const selectedToolIds = ref(new Set())
const httpTools = ref([])
const loading = ref(false)
const loadError = ref('')

const activeTools = computed(() => {
  if (activeTransport.value !== 'HTTP') {
    return []
  }

  const keyword = searchText.value.trim().toLowerCase()
  if (!keyword) {
    return httpTools.value
  }

  return httpTools.value.filter((tool) => {
    return [tool.name, tool.description, tool.endpoint]
      .filter(Boolean)
      .some((value) => value.toLowerCase().includes(keyword))
  })
})

const selectedCount = computed(() => selectedToolIds.value.size)

function toolId(tool) {
  return tool.id ?? [tool.requestMethod ?? 'HTTP', tool.endpoint ?? '', tool.name ?? ''].join(':')
}

function changeTransport(transport) {
  activeTransport.value = transport
  selectedTool.value = null
  searchText.value = ''
}

function handleToolClick(tool) {
  if (!isBatchMode.value) {
    selectedTool.value = tool
    return
  }

  const next = new Set(selectedToolIds.value)
  const id = toolId(tool)
  next.has(id) ? next.delete(id) : next.add(id)
  selectedToolIds.value = next
}

function toggleBatchMode() {
  isBatchMode.value = !isBatchMode.value
  if (!isBatchMode.value) {
    selectedToolIds.value = new Set()
  }
}

function clearSelection() {
  selectedToolIds.value = new Set()
}

async function loadTools() {
  loading.value = true
  loadError.value = ''

  try {
    const response = await fetch('/httpDefinitionTool/get')
    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`)
    }
    const payload = await response.json()
    httpTools.value = Array.isArray(payload) ? payload : Object.values(payload)
  } catch (error) {
    loadError.value = `加载 HTTP Tool 失败：${error.message}`
  } finally {
    loading.value = false
  }
}

onMounted(loadTools)
</script>

<template>
  <main class="app-shell">
    <header class="app-header glass-surface">
      <div>
        <p class="eyebrow">MCP SERVER GENERATE</p>
        <h1>Tool Workspace</h1>
      </div>

      <div class="header-actions">
        <button class="secondary-button" type="button" @click="loadTools">
          刷新工具
        </button>
        <button class="primary-button" type="button" @click="toggleBatchMode">
          {{ isBatchMode ? '完成' : '选择工具' }}
        </button>
      </div>
    </header>

    <section class="workspace-layout">
      <ToolSidebar
        :active-transport="activeTransport"
        :tools="activeTools"
        :search-text="searchText"
        :loading="loading"
        :error-message="loadError"
        :batch-mode="isBatchMode"
        :selected-tool-ids="selectedToolIds"
        :active-tool="selectedTool"
        @update:transport="changeTransport"
        @update:search-text="searchText = $event"
        @select-tool="handleToolClick"
      />

      <ToolDetailWorkspace
        :transport="activeTransport"
        :tool="selectedTool"
      />
    </section>

    <BatchActionBar
      v-if="isBatchMode"
      :selected-count="selectedCount"
      @clear="clearSelection"
    />
  </main>
</template>
