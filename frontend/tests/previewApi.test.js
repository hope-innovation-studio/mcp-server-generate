import test from 'node:test'
import assert from 'node:assert/strict'
import { previewHttpTsCode, updateHttpTsCode } from '../src/api/preview.js'

test('previewHttpTsCode posts the current template model and returns rendered code', async () => {
  const originalFetch = globalThis.fetch
  let request
  globalThis.fetch = async (url, options) => {
    request = { url: String(url), options }
    return {
      ok: true,
      async json() {
        return {
          code: 200,
          message: 'success',
          data: {
            path: 'default/queryOrder.ts',
            language: 'typescript',
            content: 'export class QueryOrderTool {}',
          },
        }
      },
    }
  }

  const payload = {
    nodeId: 'file-1',
    tsHttpToolTemplateModel: {
      className: 'QueryOrderTool',
      toolName: 'queryOrder',
    },
  }

  try {
    const preview = await previewHttpTsCode(payload)

    assert.equal(request.url, '/generate/preview-ts-http-code')
    assert.equal(request.options.method, 'POST')
    assert.equal(request.options.headers['Content-Type'], 'application/json')
    assert.deepEqual(JSON.parse(request.options.body), payload)
    assert.equal(preview.content, 'export class QueryOrderTool {}')
  } finally {
    globalThis.fetch = originalFetch
  }
})

test('updateHttpTsCode posts the current template model to the update endpoint', async () => {
  const originalFetch = globalThis.fetch
  let request
  globalThis.fetch = async (url, options) => {
    request = { url: String(url), options }
    return {
      ok: true,
      async json() {
        return {
          code: 200,
          message: 'success',
          data: { path: 'default/queryOrder.ts', language: 'typescript', content: 'saved code' },
        }
      },
    }
  }

  const payload = {
    nodeId: 'file-1',
    tsHttpToolTemplateModel: { className: 'QueryOrderTool', toolName: 'queryOrder' },
  }

  try {
    const preview = await updateHttpTsCode(payload)

    assert.equal(request.url, '/generate/update-ts-http-code')
    assert.equal(request.options.method, 'POST')
    assert.equal(request.options.headers['Content-Type'], 'application/json')
    assert.deepEqual(JSON.parse(request.options.body), payload)
    assert.equal(preview.content, 'saved code')
  } finally {
    globalThis.fetch = originalFetch
  }
})
