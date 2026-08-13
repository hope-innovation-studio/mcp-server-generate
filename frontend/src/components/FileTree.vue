<script setup>
import FileTreeNode from './FileTreeNode.vue'
defineProps({ root: { type: Object, default: null }, selectedId: { type: String, default: '' } })
defineEmits(['select', 'action'])
</script>
<template><div class="file-tree"><FileTreeNode v-if="root" :node="root" :selected-id="selectedId" @select="$emit('select', $event)" @action="$emit('action', $event)" /></div></template>
