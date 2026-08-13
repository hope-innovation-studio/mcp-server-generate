<script setup>
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { getToolList } from './api/tool.js'
import { createFolder, getFileTree, initializeFrameworkTree } from './api/fileTree.js'
import ToolSidebar from './components/ToolSidebar.vue'
import ProjectExplorer from './components/ProjectExplorer.vue'
import EditorWorkspace from './components/EditorWorkspace.vue'
import { clampPanelWidth, resizePanel } from './utils/panelResize.js'

const activeTransport = ref('HTTP')
const searchText = ref('')
const selectedTool = ref(null)
const selectedNode = ref(null)
const tools = ref([])
const fileTree = ref(null)
const toolLoading = ref(false)
const treeLoading = ref(false)
const toolError = ref('')
const treeError = ref('')
const frameworkInitializing = ref(false)
const openTabs = ref([])
const activeTabId = ref('')
const leftPanelWidth = ref(270)
const rightPanelWidth = ref(280)
let resizeState = null
let localId = 0

const workspaceStyle = computed(() => ({
  '--left-panel-width': `${leftPanelWidth.value}px`,
  '--right-panel-width': `${rightPanelWidth.value}px`,
}))

const activeTab = computed(() =>
  openTabs.value.find((tab) => tab.id === activeTabId.value) || null,
)

const visibleTools = computed(() => {
  if (activeTransport.value !== 'HTTP') return []
  const keyword = searchText.value.trim().toLowerCase()
  if (!keyword) return tools.value
  return tools.value.filter((tool) =>
    [tool.name, tool.description, tool.endpoint]
      .filter(Boolean)
      .some((value) => String(value).toLowerCase().includes(keyword)),
  )
})

async function loadTools() {
  toolLoading.value = true
  toolError.value = ''
  try {
    tools.value = await getToolList()
    if (!selectedTool.value && tools.value.length) selectTool(tools.value[0])
  } catch (error) {
    toolError.value = error.message
  } finally {
    toolLoading.value = false
  }
}

async function loadTree() {
  treeLoading.value = true
  treeError.value = ''
  try {
    fileTree.value = await getFileTree()
    selectedNode.value = fileTree.value
  } catch (error) {
    treeError.value = error.message
  } finally {
    treeLoading.value = false
  }
}

function nodeType(node) {
  return node?.nodeType || (Array.isArray(node?.children) ? 'FOLDER' : 'STATIC_FILE')
}

function appendNode(parent, child) {
  if (!parent || nodeType(parent) !== 'FOLDER') return
  if (!Array.isArray(parent.children)) parent.children = []
  parent.children.push(child)
}

function createLocalNode(type, name, parent = selectedNode.value) {
  const parentPath = parent?.path || ''
  return {
    id: `local-${Date.now()}-${localId++}`,
    path: [parentPath, name].filter(Boolean).join('/'),
    nodeType: type,
    ...(type === 'FOLDER' ? { children: [] } : {}),
  }
}

async function addFolder({ parent, name }) {
  treeError.value = ''
  try {
    const newFolder = await createFolder({
      parentId: parent.id,
      pathName: name,
    })
    appendNode(parent, newFolder)
  } catch (error) {
    treeError.value = error.message
  }
}

async function initializeFramework() {
  frameworkInitializing.value = true
  treeError.value = ''
  try {
    fileTree.value = await initializeFrameworkTree()
    selectedNode.value = fileTree.value
  } catch (error) {
    treeError.value = error.message
  } finally {
    frameworkInitializing.value = false
  }
}

function addFile({ parent, name }) {
  appendNode(parent, createLocalNode('STATIC_FILE', name, parent))
}

function addToolFile(fileName) {
  if (!selectedTool.value) return
  appendNode(selectedNode.value, {
    ...createLocalNode('TEMPLATE_FILE', fileName),
    toolId: selectedTool.value.id,
    toolName: selectedTool.value.name,
  })
}

function selectTool(tool) {
  selectedTool.value = tool
  openTab({ id: `tool:${tool.id}`, type: 'tool', title: tool.name || '未命名 Tool', tool })
}

function selectNode(node) {
  selectedNode.value = node
  if (nodeType(node) !== 'FOLDER') {
    openTab({
      id: `file:${node.id}`,
      type: 'file',
      title: node.path?.split('/').filter(Boolean).at(-1) || '未命名文件',
      node,
    })
  }
}

function openTab(tab) {
  if (!openTabs.value.some((item) => item.id === tab.id)) openTabs.value.push(tab)
  activeTabId.value = tab.id
}

function activateTab(tabId) {
  activeTabId.value = tabId
  const tab = openTabs.value.find((item) => item.id === tabId)
  if (tab?.type === 'tool') selectedTool.value = tab.tool
}

function closeTab(tabId) {
  const index = openTabs.value.findIndex((tab) => tab.id === tabId)
  if (index < 0) return
  const wasActive = activeTabId.value === tabId
  openTabs.value.splice(index, 1)
  if (wasActive) {
    const nextTab = openTabs.value[Math.min(index, openTabs.value.length - 1)]
    activateTab(nextTab?.id || '')
  }
}

function findParent(node, targetId) {
  if (!Array.isArray(node?.children)) return null
  if (node.children.some((child) => child.id === targetId)) return node
  for (const child of node.children) {
    const parent = findParent(child, targetId)
    if (parent) return parent
  }
  return null
}

function renameNode({ node, name }) {
  const parts = node.path.split('/').filter(Boolean)
  parts[parts.length - 1] = name
  node.path = parts.join('/')
  const tab = openTabs.value.find((item) => item.id === `file:${node.id}`)
  if (tab) tab.title = name
}

function deleteNode(node) {
  if (node === fileTree.value) return
  const parent = findParent(fileTree.value, node.id)
  if (!parent) return
  parent.children = parent.children.filter((child) => child.id !== node.id)
  closeTab(`file:${node.id}`)
  if (selectedNode.value?.id === node.id) selectedNode.value = parent
}

function startResize(side, event) {
  resizeState = {
    side,
    startX: event.clientX,
    startWidth: side === 'left' ? leftPanelWidth.value : rightPanelWidth.value,
  }
  document.body.classList.add('is-resizing-panels')
  window.addEventListener('pointermove', handleResize)
  window.addEventListener('pointerup', stopResize, { once: true })
}

function handleResize(event) {
  if (!resizeState) return
  const limits = resizeState.side === 'left' ? { min: 220, max: 520 } : { min: 220, max: 440 }
  const width = resizePanel({ ...resizeState, currentX: event.clientX, ...limits })
  if (resizeState.side === 'left') leftPanelWidth.value = width
  else rightPanelWidth.value = width
}

function stopResize() {
  window.removeEventListener('pointermove', handleResize)
  document.body.classList.remove('is-resizing-panels')
  resizeState = null
  localStorage.setItem('mcp-generator:left-panel-width', String(leftPanelWidth.value))
  localStorage.setItem('mcp-generator:right-panel-width', String(rightPanelWidth.value))
}

function resetPanel(side) {
  if (side === 'left') leftPanelWidth.value = 270
  else rightPanelWidth.value = 280
  const value = side === 'left' ? leftPanelWidth.value : rightPanelWidth.value
  localStorage.setItem(`mcp-generator:${side}-panel-width`, String(value))
}

onMounted(() => {
  leftPanelWidth.value = clampPanelWidth(Number(localStorage.getItem('mcp-generator:left-panel-width')) || 270, 220, 520)
  rightPanelWidth.value = clampPanelWidth(Number(localStorage.getItem('mcp-generator:right-panel-width')) || 280, 220, 440)
  loadTools()
  loadTree()
})

onBeforeUnmount(() => {
  window.removeEventListener('pointermove', handleResize)
  document.body.classList.remove('is-resizing-panels')
})
</script>

<template>
  <main class="app-shell">
    <header class="app-header glass-surface">
      <div>
        <p class="eyebrow">MCP SERVER GENERATE</p>
        <h1>生成工作台</h1>
      </div>
      <div class="project-identity">
        <span class="status-dot"></span>
        <span>default project</span>
      </div>
      <div class="header-actions">
        <button class="secondary-button" type="button" @click="loadTools">刷新 Tool</button>
        <button class="primary-button" type="button" disabled>生成完整项目</button>
      </div>
    </header>

    <section class="workspace-layout" :style="workspaceStyle">
      <ProjectExplorer
        :tree="fileTree"
        :selected-node="selectedNode"
        :loading="treeLoading"
        :initializing="frameworkInitializing"
        :error-message="treeError"
        @refresh="loadTree"
        @initialize-framework="initializeFramework"
        @select-node="selectNode"
        @add-folder="addFolder"
        @add-file="addFile"
        @rename-node="renameNode"
        @delete-node="deleteNode"
      />

      <div
        class="panel-resizer left-resizer"
        role="separator"
        aria-label="调整文件树宽度"
        @pointerdown.prevent="startResize('left', $event)"
        @dblclick="resetPanel('left')"
      ></div>

      <EditorWorkspace
        :tabs="openTabs"
        :active-tab="activeTab"
        :selected-node="selectedNode"
        @activate-tab="activateTab"
        @close-tab="closeTab"
        @add-tool="addToolFile"
      />

      <div
        class="panel-resizer right-resizer"
        role="separator"
        aria-label="调整 Tool 列表宽度"
        @pointerdown.prevent="startResize('right', $event)"
        @dblclick="resetPanel('right')"
      ></div>

      <ToolSidebar
        :active-transport="activeTransport"
        :tools="visibleTools"
        :search-text="searchText"
        :loading="toolLoading"
        :error-message="toolError"
        :active-tool="selectedTool"
        @update:transport="activeTransport = $event"
        @update:search-text="searchText = $event"
        @select-tool="selectTool"
      />
    </section>
  </main>
</template>
