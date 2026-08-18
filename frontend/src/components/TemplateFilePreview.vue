<script setup>
import { computed, onBeforeUnmount, reactive, ref, watch } from 'vue'
import { VueMonacoEditor } from '@guolao/vue-monaco-editor'
import { previewHttpTsCode, updateHttpTsCode } from '../api/preview.js'
import {
  PREVIEW_DEBOUNCE_MS,
  createPreviewRequest,
  createTemplateVariables,
} from '../utils/templatePreview.js'
import { groupRequestParameters } from '../utils/requestConfig.js'

const props = defineProps({
  node: { type: Object, required: true },
})
const emit = defineEmits(['saved'])

const variables = reactive(createTemplateVariables(props.node))
const container = ref(null)
const variableWidth = ref(42)
const activeSection = ref('params')
const code = ref('')
const previewPath = ref(props.node.path)
const previewLanguage = ref('typescript')
const previewLoading = ref(false)
const previewError = ref('')
const saving = ref(false)
const saveState = ref('idle')
const saveError = ref('')
const savedTemplateSnapshot = ref('')
let resizeStart = null
let previewTimer = null
let previewVersion = 0

const groupedParameters = computed(() => groupRequestParameters(variables.parameters))
const visibleParameters = computed(() => groupedParameters.value[activeSection.value] || [])
const currentTemplateSnapshot = computed(() => snapshotTemplateModel(
  createPreviewRequest(props.node.id, variables).tsHttpToolTemplateModel,
))
const hasUnsavedChanges = computed(() => (
  currentTemplateSnapshot.value !== savedTemplateSnapshot.value
))
const requestSections = [
  { id: 'params', label: 'Params' },
  { id: 'body', label: 'Body' },
  { id: 'headers', label: 'Headers' },
  { id: 'cookies', label: 'Cookies' },
  { id: 'tool', label: 'Tool' },
]
const editorOptions = {
  automaticLayout: true,
  readOnly: true,
  minimap: { enabled: false },
  fontSize: 13,
  lineHeight: 21,
  scrollBeyondLastLine: false,
  renderLineHighlight: 'none',
  padding: { top: 16 },
}

watch(() => props.node.id, () => {
  Object.assign(variables, createTemplateVariables(props.node))
  savedTemplateSnapshot.value = currentTemplateSnapshot.value
  previewPath.value = props.node.path
  schedulePreview()
}, { immediate: true })

watch(variables, schedulePreview, { deep: true })

function snapshotTemplateModel(templateModel) {
  return JSON.stringify(templateModel)
}

function schedulePreview() {
  window.clearTimeout(previewTimer)
  const version = ++previewVersion
  previewLoading.value = true
  previewError.value = ''
  saveState.value = 'idle'
  saveError.value = ''
  previewTimer = window.setTimeout(() => loadPreview(version), PREVIEW_DEBOUNCE_MS)
}

async function loadPreview(version) {
  try {
    const preview = await previewHttpTsCode(
      createPreviewRequest(props.node.id, variables),
    )
    if (version !== previewVersion) return

    code.value = preview.content || ''
    previewPath.value = preview.path || props.node.path
    previewLanguage.value = preview.language || 'typescript'
  } catch (error) {
    if (version !== previewVersion) return
    previewError.value = error instanceof Error ? error.message : '代码预览失败'
  } finally {
    if (version === previewVersion) previewLoading.value = false
  }
}

async function saveChanges() {
  if (!hasUnsavedChanges.value) return

  window.clearTimeout(previewTimer)
  const version = ++previewVersion
  const payload = createPreviewRequest(props.node.id, variables)
  const payloadSnapshot = snapshotTemplateModel(payload.tsHttpToolTemplateModel)
  saving.value = true
  previewLoading.value = true
  previewError.value = ''
  saveState.value = 'idle'
  saveError.value = ''

  try {
    const preview = await updateHttpTsCode(payload)
    savedTemplateSnapshot.value = payloadSnapshot
    emit('saved', {
      nodeId: payload.nodeId,
      templateModel: payload.tsHttpToolTemplateModel,
    })

    if (version !== previewVersion) return
    code.value = preview.content || ''
    previewPath.value = preview.path || props.node.path
    previewLanguage.value = preview.language || 'typescript'
    saveState.value = 'saved'
  } catch (error) {
    if (version !== previewVersion) return
    saveState.value = 'error'
    saveError.value = error instanceof Error ? error.message : '保存失败'
  } finally {
    saving.value = false
    if (version === previewVersion) previewLoading.value = false
  }
}

function startResize(event) {
  resizeStart = { x: event.clientX, width: variableWidth.value }
  document.body.classList.add('is-resizing-panels')
  window.addEventListener('pointermove', resize)
  window.addEventListener('pointerup', stopResize, { once: true })
}

function resize(event) {
  if (!resizeStart || !container.value) return
  const deltaPercent = ((event.clientX - resizeStart.x) / container.value.clientWidth) * 100
  variableWidth.value = Math.min(62, Math.max(28, resizeStart.width + deltaPercent))
}

function stopResize() {
  resizeStart = null
  document.body.classList.remove('is-resizing-panels')
  window.removeEventListener('pointermove', resize)
}

onBeforeUnmount(() => {
  window.clearTimeout(previewTimer)
  previewVersion += 1
  stopResize()
})
</script>

<template>
  <div
    ref="container"
    class="template-preview"
    :style="{ '--variable-pane-width': `${variableWidth}%` }"
  >
    <section class="template-variables-pane request-builder-pane">
      <div class="request-address-bar">
        <select v-model="variables.requestMethod" :class="`method-${variables.requestMethod.toLowerCase()}`">
          <option>GET</option><option>POST</option><option>PUT</option><option>PATCH</option><option>DELETE</option>
        </select>
        <input v-model="variables.url" aria-label="请求地址" />
      </div>

      <nav class="request-section-tabs" aria-label="请求配置">
        <button
          v-for="section in requestSections"
          :key="section.id"
          type="button"
          :class="{ active: activeSection === section.id }"
          @click="activeSection = section.id"
        >
          {{ section.label }}
          <small v-if="section.id !== 'tool'">{{ groupedParameters[section.id].length }}</small>
        </button>
      </nav>

      <div class="request-panel-scroll">
        <div v-if="activeSection === 'tool'" class="request-tool-form template-form">
          <div class="template-form-row">
            <label><span>类名</span><input v-model="variables.className" /></label>
            <label><span>Tool 名称</span><input v-model="variables.toolName" /></label>
          </div>
          <label><span>Tool 描述</span><textarea v-model="variables.description" rows="4"></textarea></label>
          <label><span>文件类型</span><input value="TypeScript" disabled /></label>
        </div>

        <div v-else class="request-parameter-panel">
          <div class="request-table-caption">
            <div>
              <p class="eyebrow">{{ activeSection.toUpperCase() }}</p>
              <strong>{{ requestSections.find((section) => section.id === activeSection)?.label }} 参数</strong>
            </div>
            <span>{{ visibleParameters.length }} 项</span>
          </div>

          <div v-if="visibleParameters.length" class="request-parameter-table">
            <div class="request-parameter-head">
              <span>参数名</span><span>类型</span><span>位置</span><span>必填</span><span>说明</span>
            </div>
            <div
              v-for="(parameter, index) in visibleParameters"
              :key="parameter.id || `${parameter.location}:${index}`"
              class="request-parameter-row"
            >
              <input v-model="parameter.key" />
              <code>{{ parameter.zodSchema || 'z.unknown()' }}</code>
              <span class="location-badge">{{ parameter.location }}</span>
              <span :class="['required-state', { optional: !parameter.required }]">{{ parameter.required ? '是' : '否' }}</span>
              <input v-model="parameter.description" placeholder="添加说明" />
            </div>
          </div>
          <div v-else class="request-empty-state">
            <span>＋</span>
            <p>当前请求没有 {{ requestSections.find((section) => section.id === activeSection)?.label }} 参数</p>
          </div>
        </div>
      </div>

      <footer class="request-save-bar">
        <button
          class="preview-save-button"
          :class="[saveState, { dirty: hasUnsavedChanges }]"
          type="button"
          :disabled="saving || !hasUnsavedChanges"
          :title="saveError || (hasUnsavedChanges ? '存在未保存的修改' : '当前修改已保存')"
          @click="saveChanges"
        >
          <span v-if="hasUnsavedChanges && !saving" class="save-reminder-dot" aria-hidden="true"></span>
          {{ saving ? '保存中…' : saveState === 'error' ? '保存失败' : hasUnsavedChanges ? '保存修改' : '已保存' }}
        </button>
      </footer>
    </section>

    <div class="template-splitter" @pointerdown.prevent="startResize"></div>

    <section class="code-preview-pane">
      <header class="code-preview-header">
        <div><span class="status-dot"></span><strong>{{ previewPath }}</strong></div>
        <span>只读预览 · {{ previewLanguage }}</span>
      </header>
      <div class="monaco-shell">
        <div v-if="previewError" class="editor-loading error-message">{{ previewError }}</div>
        <div v-else-if="previewLoading && !code" class="editor-loading">正在生成代码预览…</div>
        <VueMonacoEditor
          v-else
          :value="code"
          :path="previewPath"
          :language="previewLanguage"
          theme="vs"
          height="100%"
          :options="editorOptions"
        >
          <template #default><div class="editor-loading">正在加载代码预览…</div></template>
          <template #failure><div class="editor-loading error-message">代码编辑器加载失败</div></template>
        </VueMonacoEditor>
      </div>
      <footer class="code-statusbar"><span>{{ previewLanguage }}</span><span>UTF-8</span><span>只读</span></footer>
    </section>
  </div>
</template>
