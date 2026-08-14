<script setup>
import { computed, onBeforeUnmount, reactive, ref, watch } from 'vue'
import { VueMonacoEditor } from '@guolao/vue-monaco-editor'
import { createTemplateVariables, renderStaticToolCode } from '../utils/templatePreview.js'

const props = defineProps({
  node: { type: Object, required: true },
})

const variables = reactive(createTemplateVariables(props.node))
const container = ref(null)
const variableWidth = ref(42)
let resizeStart = null

const code = computed(() => renderStaticToolCode(variables))
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
})

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

onBeforeUnmount(stopResize)
</script>

<template>
  <div
    ref="container"
    class="template-preview"
    :style="{ '--variable-pane-width': `${variableWidth}%` }"
  >
    <section class="template-variables-pane">
      <header class="template-pane-header">
        <div>
          <p class="eyebrow">TEMPLATE VARIABLES</p>
          <h2>模板变量</h2>
        </div>
        <span>{{ variables.parameters.length }} 个参数</span>
      </header>

      <div class="template-form">
        <label>
          <span>Tool 名称</span>
          <input v-model="variables.toolName" />
        </label>
        <label>
          <span>描述</span>
          <textarea v-model="variables.description" rows="3"></textarea>
        </label>
        <div class="template-form-row">
          <label>
            <span>请求方法</span>
            <select v-model="variables.requestMethod">
              <option>GET</option><option>POST</option><option>PUT</option><option>PATCH</option><option>DELETE</option>
            </select>
          </label>
          <label>
            <span>文件类型</span>
            <input value="TypeScript" disabled />
          </label>
        </div>
        <label>
          <span>请求地址</span>
          <input v-model="variables.url" />
        </label>
      </div>

      <div class="template-parameter-block">
        <div class="template-section-title">
          <span>请求参数</span><small>{{ variables.parameters.length }}</small>
        </div>
        <div v-if="variables.parameters.length" class="template-parameter-list">
          <div v-for="parameter in variables.parameters" :key="parameter.key" class="template-parameter-item">
            <strong>{{ parameter.key }}</strong>
            <code>{{ parameter.zodSchema || 'z.unknown()' }}</code>
            <span>{{ parameter.location }}</span>
          </div>
        </div>
        <p v-else class="template-empty-parameters">当前模板没有请求参数</p>
      </div>
    </section>

    <div class="template-splitter" @pointerdown.prevent="startResize"></div>

    <section class="code-preview-pane">
      <header class="code-preview-header">
        <div><span class="status-dot"></span><strong>{{ node.path }}</strong></div>
        <span>只读预览 · TypeScript</span>
      </header>
      <div class="monaco-shell">
        <VueMonacoEditor
          :value="code"
          :path="node.path"
          language="typescript"
          theme="vs"
          height="100%"
          :options="editorOptions"
        >
          <template #default><div class="editor-loading">正在加载代码预览…</div></template>
          <template #failure><div class="editor-loading error-message">代码编辑器加载失败</div></template>
        </VueMonacoEditor>
      </div>
      <footer class="code-statusbar"><span>TypeScript</span><span>UTF-8</span><span>只读</span></footer>
    </section>
  </div>
</template>
