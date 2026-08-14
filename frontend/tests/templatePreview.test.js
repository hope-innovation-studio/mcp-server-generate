import test from 'node:test'
import assert from 'node:assert/strict'
import { createTemplateVariables, renderStaticToolCode } from '../src/utils/templatePreview.js'

test('creates preview variables from a template file node', () => {
  const variables = createTemplateVariables({
    toolTemplateModel: {
      toolName: 'queryOrder',
      description: '查询订单',
      requestMethod: 'GET',
      url: 'http://localhost:8080/orders/{id}',
    },
  })

  assert.equal(variables.toolName, 'queryOrder')
  assert.equal(variables.requestMethod, 'GET')
  assert.equal(variables.url, 'http://localhost:8080/orders/{id}')
})

test('renders static TypeScript preview with current variables', () => {
  const code = renderStaticToolCode({
    toolName: 'queryOrder',
    description: '查询订单',
    requestMethod: 'GET',
    url: 'http://localhost:8080/orders/{id}',
  })

  assert.match(code, /class QueryOrderTool/)
  assert.match(code, /method: "GET"/)
  assert.match(code, /查询订单/)
})
