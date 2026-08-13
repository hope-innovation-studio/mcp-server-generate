<script setup>
import FileTree from './FileTree.vue'

const props = defineProps({
  tree: { type: Object, default: null },
  selectedNode: { type: Object, default: null },
  loading: Boolean,
  initializing: Boolean,
  errorMessage: String,
})

const emit = defineEmits(['select-node', 'add-folder', 'add-file', 'rename-node', 'delete-node', 'refresh', 'initialize-framework'])

function handleNodeAction({ action, node }) {
  if (action === 'delete') {
    if (window.confirm(`确定删除“${node.path}”吗？`)) emit('delete-node', node)
    return
  }
  const prompts = {
    'add-file': ['新文件名称', 'new-file.ts'],
    'add-folder': ['新文件夹名称', 'new-folder'],
    rename: ['新的名称', node.path?.split('/').filter(Boolean).at(-1) || ''],
  }
  const config = prompts[action]
  if (!config) return
  const name = window.prompt(config[0], config[1])?.trim()
  if (!name) return
  if (action === 'rename') emit('rename-node', { node, name })
  else emit(action, { parent: node, name })
}
</script>

<template>
  <aside class="explorer-panel glass-surface">
    <header class="side-panel-header">
      <div>
        <p class="eyebrow">EXPLORER</p>
        <h2>项目文件</h2>
      </div>
      <div class="explorer-header-actions">
        <button
          class="icon-button"
          type="button"
          title="初始化 TypeScript Framework"
          :disabled="initializing"
          @click="$emit('initialize-framework')"
        >
          {{ initializing ? '…' : '</>' }}
        </button>
        <button class="icon-button" type="button" title="刷新文件树" @click="$emit('refresh')">↻</button>
      </div>
    </header>

    <p v-if="loading" class="panel-message">正在加载文件树…</p>
    <p v-else-if="errorMessage" class="panel-message error-message">{{ errorMessage }}</p>
    <FileTree
      v-else
      :root="tree"
      :selected-id="selectedNode?.id"
      @select="$emit('select-node', $event)"
      @action="handleNodeAction"
    />

    <footer class="explorer-footer">
      <span class="status-dot"></span>
      <span>{{ selectedNode?.path || '尚未选择节点' }}</span>
    </footer>
  </aside>
</template>
