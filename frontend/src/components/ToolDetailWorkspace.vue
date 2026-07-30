<script setup>
defineProps({
  transport: { type: String, required: true },
  tool: { type: Object, default: null },
})

function methodLabel(tool) {
  return tool?.requestMethod || 'HTTP'
}

function parameterType(parameter) {
  return parameter.type?.type || '未知类型'
}

function formatSchema(schema) {
  return schema ? JSON.stringify(schema, null, 2) : '暂无结构信息'
}
</script>

<template>
  <section class="tool-detail glass-surface">
    <div v-if="transport !== 'HTTP'" class="empty-workspace">
      <span class="empty-icon">◌</span>
      <h2>{{ transport === 'SSE' ? 'SSE Tool' : 'WebSocket Tool' }}</h2>
      <p>当前通信方式还没有可展示的 Tool。</p>
    </div>

    <div v-else-if="!tool" class="empty-workspace">
      <span class="empty-icon">⌁</span>
      <h2>选择一个 Tool</h2>
      <p>从左侧工具列表选择接口，查看它的完整 HTTP 定义。</p>
    </div>

    <template v-else>
      <header class="detail-header">
        <p class="eyebrow">HTTP TOOL</p>
        <h2>{{ tool.name }}</h2>
        <p>{{ tool.description || '暂无 Tool 描述' }}</p>
      </header>

      <div class="detail-grid">
        <article class="detail-card endpoint-card">
          <span class="card-label">ENDPOINT</span>
          <div class="endpoint-line">
            <span class="method-label" :class="`method-${methodLabel(tool).toLowerCase()}`">
              {{ methodLabel(tool) }}
            </span>
            <code>{{ tool.endpoint || '/' }}</code>
          </div>
          <p>{{ tool.consumes || '未声明请求体格式' }}</p>
        </article>

        <article class="detail-card">
          <span class="card-label">REQUEST PARAMETERS</span>
          <div v-if="tool.parameters?.length" class="parameter-list">
            <div v-for="parameter in tool.parameters" :key="parameter.key" class="parameter-row">
              <div>
                <strong>{{ parameter.key }}</strong>
                <span>{{ parameter.location || 'UN_KNOW' }} · {{ parameterType(parameter) }}</span>
              </div>
              <em :class="{ optional: !parameter.required }">
                {{ parameter.required ? '必填' : '可选' }}
              </em>
            </div>
          </div>
          <p v-else class="muted-text">该接口没有请求参数。</p>
        </article>

        <article class="detail-card">
          <span class="card-label">REQUEST BODY</span>
          <strong>{{ tool.consumes || '无请求体' }}</strong>
          <p class="muted-text">请求体 Schema 将在 OpenAPI 解析接入后展示。</p>
        </article>

        <article class="detail-card response-card">
          <span class="card-label">RESPONSE</span>
          <strong>{{ tool.produces || 'application/json' }}</strong>
          <pre class="schema-code">{{ formatSchema(tool.returnType) }}</pre>
        </article>
      </div>
    </template>
  </section>
</template>
