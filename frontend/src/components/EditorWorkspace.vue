<script setup>
import { computed, nextTick, ref, watch } from 'vue'
import { generateTool } from '../api/tool.js'
import { calculateVisibleScrollLeft } from '../utils/tabScroll.js'
import TemplateFilePreview from './TemplateFilePreview.vue'

const props = defineProps({
  tabs: { type: Array, default: () => [] },
  activeTab: { type: Object, default: null },
  selectedNode: { type: Object, default: null },
  addingTool: Boolean,
  addToolError: { type: String, default: '' },
})

const emit = defineEmits(['activate-tab', 'close-tab', 'add-tool'])
const generating = ref(false)
const resultMessage = ref('')
const errorMessage = ref('')
const fileName = ref('')
const tabbar = ref(null)

const tool = computed(() => props.activeTab?.type === 'tool' ? props.activeTab.tool : null)
const fileNode = computed(() => props.activeTab?.type === 'file' ? props.activeTab.node : null)
const nodeName = computed(() => fileNode.value?.path?.split('/').filter(Boolean).at(-1) || '未命名文件')
const nodeType = computed(() =>
  props.selectedNode?.nodeType || (Array.isArray(props.selectedNode?.children) ? 'FOLDER' : 'STATIC_FILE'),
)
const canAddTool = computed(() => nodeType.value === 'FOLDER')

watch(() => tool.value?.id, () => {
  fileName.value = tool.value?.name ? `${tool.value.name}.ts` : ''
  resultMessage.value = ''
  errorMessage.value = ''
}, { immediate: true })

watch(() => props.activeTab?.id, async (activeId) => {
  if (!activeId) return
  await nextTick()
  const container = tabbar.value
  const activeElement = container?.querySelector('.editor-tab.active')
  if (!container || !activeElement) return
  container.scrollTo({
    left: calculateVisibleScrollLeft({
      scrollLeft: container.scrollLeft,
      viewportWidth: container.clientWidth,
      tabLeft: activeElement.offsetLeft,
      tabWidth: activeElement.offsetWidth,
    }),
    behavior: 'smooth',
  })
})

function scrollTabs(event) {
  if (!tabbar.value || tabbar.value.scrollWidth <= tabbar.value.clientWidth) return
  event.preventDefault()
  tabbar.value.scrollLeft += event.deltaY || event.deltaX
}

function formatSchema(schema) {
  return schema ? JSON.stringify(schema, null, 2) : '暂无结构信息'
}

async function handleGenerate() {
  if (!tool.value) return
  generating.value = true
  resultMessage.value = ''
  errorMessage.value = ''
  try {
    resultMessage.value = await generateTool({
      toolName: tool.value.name,
      toolId: tool.value.id,
      projectName: 'quick-preview',
    })
  } catch (error) {
    errorMessage.value = error.message
  } finally {
    generating.value = false
  }
}

function addCurrentTool() {
  if (!tool.value || !canAddTool.value) return
  emit('add-tool', fileName.value.trim() || `${tool.value.name}.ts`)
}
</script>

<template>
  <section class="editor-panel glass-surface">
    <div ref="tabbar" class="editor-tabbar" @wheel="scrollTabs">
      <div
        v-for="tab in tabs"
        :key="tab.id"
        class="editor-tab"
        :class="{ active: activeTab?.id === tab.id }"
        role="button"
        tabindex="0"
        @click="$emit('activate-tab', tab.id)"
        @keydown.enter="$emit('activate-tab', tab.id)"
      >
        <span class="tab-mark" :class="tab.type === 'tool' ? 'tool-mark' : 'file-mark'"></span>
        <span class="tab-title">{{ tab.title }}</span>
        <button class="tab-close" type="button" title="关闭" @click.stop="$emit('close-tab', tab.id)">×</button>
      </div>
    </div>

    <div v-if="activeTab?.type === 'file'" class="editor-content file-document">
      <TemplateFilePreview v-if="fileNode?.nodeType === 'TEMPLATE_FILE'" :node="fileNode" />
      <div v-else class="file-preview">
        <div class="file-preview-icon">&lt;/&gt;</div>
        <p class="eyebrow">{{ fileNode?.nodeType || 'FILE' }}</p>
        <h2>{{ nodeName }}</h2>
        <code>{{ fileNode?.path }}</code>
        <p>普通文件将在接入内容接口后直接显示代码。</p>
      </div>
    </div>

    <div v-else-if="!activeTab" class="editor-content empty-workspace">
      <span class="empty-icon">⌁</span>
      <h2>工作区为空</h2>
      <p>打开右侧 Tool，或者点击左侧文件开始工作。</p>
    </div>

    <div v-else class="editor-content">
      <header class="tool-titlebar">
        <div>
          <div class="title-meta">
            <span class="method-pill" :class="`method-${(tool.requestMethod || 'http').toLowerCase()}`">
              {{ tool.requestMethod || 'HTTP' }}
            </span>
            <code>{{ tool.endpoint || '/' }}</code>
          </div>
          <h1>{{ tool.name }}</h1>
          <p>{{ tool.description || '暂无 Tool 描述' }}</p>
        </div>
        <button class="primary-button" type="button" :disabled="generating" @click="handleGenerate">
          {{ generating ? '生成中…' : '生成代码' }}
        </button>
      </header>

      <p v-if="resultMessage" class="notice success-notice">{{ resultMessage }}</p>
      <p v-if="errorMessage" class="notice error-notice">{{ errorMessage }}</p>

      <div class="definition-grid">
        <section class="definition-section parameters-section">
          <div class="section-heading">
            <div><p class="eyebrow">INPUT</p><h3>请求参数</h3></div>
            <span>{{ tool.parameters?.length || 0 }} 项</span>
          </div>
          <div v-if="tool.parameters?.length" class="parameter-table">
            <div v-for="parameter in tool.parameters" :key="parameter.key" class="parameter-item">
              <div>
                <strong>{{ parameter.key }}</strong>
                <small>{{ parameter.description || '暂无参数描述' }}</small>
              </div>
              <code>{{ parameter.type?.type || 'unknown' }}</code>
              <span>{{ parameter.location }}</span>
              <em :class="{ optional: !parameter.required }">{{ parameter.required ? '必填' : '可选' }}</em>
            </div>
          </div>
          <p v-else class="empty-line">该接口没有请求参数</p>
        </section>

        <section class="definition-section schema-section">
          <div class="section-heading">
            <div><p class="eyebrow">OUTPUT</p><h3>返回结构</h3></div>
            <span>JSON Schema</span>
          </div>
          <pre class="schema-editor">{{ formatSchema(tool.returnType) }}</pre>
        </section>
      </div>

      <section class="project-action-bar">
        <div>
          <p class="eyebrow">ADD TO PROJECT</p>
          <strong>{{ canAddTool ? selectedNode.path : '请先在左侧选择目标文件夹' }}</strong>
        </div>
        <input v-model="fileName" aria-label="生成文件名" :placeholder="`${tool.name}.ts`" />
        <button class="secondary-button" type="button" :disabled="!canAddTool || addingTool" @click="addCurrentTool">
          {{ addingTool ? '添加中…' : '添加到项目' }}
        </button>
      </section>
      <p v-if="addToolError" class="notice error-notice project-action-error">{{ addToolError }}</p>
    </div>
  </section>
</template>
