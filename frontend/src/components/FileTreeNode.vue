<script setup>
import { ref } from 'vue'

const props = defineProps({
  node: { type: Object, required: true },
  selectedId: { type: String, default: '' },
})
const emit = defineEmits(['select', 'action'])
const expanded = ref(true)
const menuOpen = ref(false)

const typeOf = (node) => node.nodeType || (Array.isArray(node.children) ? 'FOLDER' : 'STATIC_FILE')
const nameOf = (path) => path?.split('/').filter(Boolean).at(-1) || '未命名'

function selectNode() {
  emit('select', props.node)
}

function toggleFolder() {
  if (typeOf(props.node) === 'FOLDER') expanded.value = !expanded.value
}

function runAction(action) {
  menuOpen.value = false
  emit('action', { action, node: props.node })
}
</script>

<template>
  <div class="tree-node">
    <div class="tree-node-line" :class="{ selected: selectedId === node.id }">
      <button type="button" class="tree-node-row" @click="selectNode" @dblclick="toggleFolder">
        <span class="tree-chevron" @click.stop="toggleFolder">{{ typeOf(node) === 'FOLDER' ? (expanded ? '⌄' : '›') : '' }}</span>
        <span class="tree-icon">{{ typeOf(node) === 'FOLDER' ? '▰' : typeOf(node) === 'TEMPLATE_FILE' ? '◆' : '▱' }}</span>
        <span class="tree-node-name">{{ nameOf(node.path) }}</span>
      </button>
      <button class="node-menu-trigger" type="button" title="节点操作" @click.stop="menuOpen = !menuOpen">⋯</button>
      <div v-if="menuOpen" class="node-menu" @mouseleave="menuOpen = false">
        <button v-if="typeOf(node) === 'FOLDER'" type="button" @click="runAction('add-file')">新建文件</button>
        <button v-if="typeOf(node) === 'FOLDER'" type="button" @click="runAction('add-folder')">新建文件夹</button>
        <span v-if="typeOf(node) === 'FOLDER'" class="menu-divider"></span>
        <button type="button" @click="runAction('rename')">重命名</button>
        <button type="button" class="danger-action" @click="runAction('delete')">删除</button>
      </div>
    </div>
    <div v-if="expanded && typeOf(node) === 'FOLDER' && node.children?.length" class="tree-children">
      <FileTreeNode
        v-for="child in node.children"
        :key="child.id"
        :node="child"
        :selected-id="selectedId"
        @select="$emit('select', $event)"
        @action="$emit('action', $event)"
      />
    </div>
  </div>
</template>
