import test from 'node:test'
import assert from 'node:assert/strict'
import {
  PREVIEW_DEBOUNCE_MS,
  createPreviewRequest,
  createTemplateVariables,
} from '../src/utils/templatePreview.js'

test('waits 800 milliseconds before requesting a new preview', () => {
  assert.equal(PREVIEW_DEBOUNCE_MS, 800)
})

test('creates preview variables from a template file node', () => {
  const variables = createTemplateVariables({
    toolTemplateModel: {
      className: 'QueryOrderTool',
      toolName: 'queryOrder',
      description: '查询订单',
      requestMethod: 'GET',
      url: 'http://localhost:8080/orders/{id}',
    },
  })

  assert.equal(variables.toolName, 'queryOrder')
  assert.equal(variables.className, 'QueryOrderTool')
  assert.equal(variables.requestMethod, 'GET')
  assert.equal(variables.url, 'http://localhost:8080/orders/{id}')
})

test('creates the backend preview request from current variables', () => {
  const request = createPreviewRequest('file-1', {
    className: 'CustomOrderTool',
    toolName: 'queryOrder',
    description: '查询订单',
    requestMethod: 'GET',
    url: 'http://localhost:8080/orders/{id}',
    parameters: [
      { key: 'id', location: 'PATH', required: true, zodSchema: 'z.string()' },
      { key: 'keyword', location: 'PARAM', required: false, zodSchema: 'z.string()' },
    ],
  })

  assert.equal(request.nodeId, 'file-1')
  assert.equal(request.tsHttpToolTemplateModel.className, 'CustomOrderTool')
  assert.equal(request.tsHttpToolTemplateModel.allTsHttpParameter.length, 2)
  assert.equal(request.tsHttpToolTemplateModel.queryTsHttpParameter.PATH[0].key, 'id')
  assert.equal(request.tsHttpToolTemplateModel.queryTsHttpParameter.PARAM[0].key, 'keyword')
})
