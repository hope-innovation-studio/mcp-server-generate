<script setup>
defineProps({
  tool: { type: Object, required: true },
  batchMode: { type: Boolean, default: false },
  selected: { type: Boolean, default: false },
  active: { type: Boolean, default: false },
})

defineEmits(['click'])

function methodLabel(tool) {
  return tool.requestMethod || 'HTTP'
}
</script>

<template>
  <button
    class="tool-sidebar-item"
    :class="{ active, selected }"
    type="button"
    @click="$emit('click')"
  >
    <span
      v-if="batchMode"
      class="selection-dot"
      :class="{ checked: selected }"
      aria-hidden="true"
    />
    <span class="method-label" :class="`method-${methodLabel(tool).toLowerCase()}`">
      {{ methodLabel(tool) }}
    </span>
    <span class="tool-name">{{ tool.name || '未命名 Tool' }}</span>
  </button>
</template>
