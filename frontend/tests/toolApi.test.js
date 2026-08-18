import test from 'node:test'
import assert from 'node:assert/strict'
import { generateTool } from '../src/api/tool.js'

test('generateTool calls the local generation endpoint with className', async () => {
  const originalFetch = globalThis.fetch
  let request
  globalThis.fetch = async (url, options) => {
    request = { url: String(url), options }
    return {
      ok: true,
      async json() {
        return { code: 200, message: 'success', data: 'generated' }
      },
    }
  }

  try {
    await generateTool({
      className: 'QueryOrderTool',
      toolName: 'queryOrder',
      toolId: 'tool-1',
      projectName: 'quick-preview',
    })

    assert.match(request.url, /^\/generate\/generate-http-ts-tool-in-local\?/)
    const query = new URLSearchParams(request.url.split('?')[1])
    assert.equal(query.get('className'), 'QueryOrderTool')
  } finally {
    globalThis.fetch = originalFetch
  }
})
