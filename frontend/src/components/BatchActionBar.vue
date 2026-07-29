<script setup>
defineProps({
  selectedCount: { type: Number, required: true },
})

defineEmits(['clear'])
</script>

<template>
  <aside class="batch-action-bar glass-surface">
    <div>
      <span class="selection-count">{{ selectedCount }}</span>
      <span>个 Tool 已选择</span>
    </div>
    <div class="batch-actions">
      <button class="text-button" type="button" @click="$emit('clear')">取消选择</button>
      <button class="primary-button" type="button" :disabled="selectedCount === 0">
        生成 MCP 项目
      </button>
    </div>
  </aside>
</template>
