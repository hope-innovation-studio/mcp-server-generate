import { requestJson, unwrapResponse } from './http.js'

export async function getToolList() {
  const data = unwrapResponse(await requestJson('/httpDefinitionTool/get-tool-list'))
  return Array.isArray(data) ? data : Object.values(data || {})
}

export async function generateTool({ toolName, toolId, projectName }) {
  const query = new URLSearchParams({ toolName, toolId, projectName })
  return unwrapResponse(await requestJson(`/generate/generate-http-ts-tool?${query}`, {
    method: 'POST',
  }))
}
