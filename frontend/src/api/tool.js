import { requestJson, unwrapResponse } from './http.js'

export async function getToolList() {
  const data = unwrapResponse(await requestJson('/httpDefinitionTool/get-tool-list'))
  return Array.isArray(data) ? data : Object.values(data || {})
}

export async function generateTool({ className, toolName, toolId, projectName }) {
  const query = new URLSearchParams({ className, toolName, toolId, projectName })
  return unwrapResponse(await requestJson(`/generate/generate-http-ts-tool-in-local?${query}`, {
    method: 'POST',
  }))
}
