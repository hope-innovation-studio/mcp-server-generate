import test from 'node:test'
import assert from 'node:assert/strict'
import { addHttpToolToFolder, createFolder, initializeFrameworkTree } from '../src/api/fileTree.js'

test('createFolder sends parent id and folder name and returns backend node', async () => {
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
          data: { id: '1001', path: 'default/src', nodeType: 'FOLDER', children: [] },
        }
      },
    }
  }

  try {
    const folder = await createFolder({ parentId: 'root-id', pathName: 'src' })

    assert.equal(request.options.method, 'POST')
    assert.match(request.url, /^\/generate\/add-folder\?/)
    const query = new URLSearchParams(request.url.split('?')[1])
    assert.equal(query.get('parentId'), 'root-id')
    assert.equal(query.get('pathName'), 'src')
    assert.equal(folder.id, '1001')
    assert.equal(folder.path, 'default/src')
  } finally {
    globalThis.fetch = originalFetch
  }
})

test('initializeFrameworkTree posts without parameters and returns the root folder', async () => {
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
          data: { id: 'root-id', path: 'default', nodeType: 'FOLDER', children: [] },
        }
      },
    }
  }

  try {
    const root = await initializeFrameworkTree()

    assert.equal(request.url, '/generate/init-framework-ts-folder')
    assert.equal(request.options.method, 'POST')
    assert.equal(root.id, 'root-id')
  } finally {
    globalThis.fetch = originalFetch
  }
})

test('addHttpToolToFolder sends tool and parent identifiers and returns the template node', async () => {
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
          data: { id: 'file-1', path: 'default/src/tool/queryOrder.ts', nodeType: 'TEMPLATE_FILE' },
        }
      },
    }
  }

  try {
    const node = await addHttpToolToFolder({
      toolName: 'queryOrder',
      parentNodeId: 'folder-1',
      toolId: 'tool-1',
    })

    assert.equal(request.options.method, 'POST')
    assert.match(request.url, /^\/generate\/add-http-ts-tool-to-folder\?/)
    const query = new URLSearchParams(request.url.split('?')[1])
    assert.equal(query.get('toolName'), 'queryOrder')
    assert.equal(query.get('parentNodeId'), 'folder-1')
    assert.equal(query.get('toolId'), 'tool-1')
    assert.equal(node.id, 'file-1')
  } finally {
    globalThis.fetch = originalFetch
  }
})
