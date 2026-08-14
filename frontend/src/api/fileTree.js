import { requestJson, unwrapResponse } from './http.js'

export async function getFileTree() {
  return unwrapResponse(await requestJson('/httpDefinitionTool/get-file-tree'))
}

export async function createFolder({ parentId, pathName }) {
  const query = new URLSearchParams({ parentId, pathName })
  return unwrapResponse(await requestJson(`/generate/add-folder?${query}`, {
    method: 'POST',
  }))
}

export async function initializeFrameworkTree() {
  return unwrapResponse(await requestJson('/generate/init-framework-ts-folder', {
    method: 'POST',
  }))
}

export async function addHttpToolToFolder({ toolName, parentNodeId, toolId }) {
  const query = new URLSearchParams({ toolName, parentNodeId, toolId })
  return unwrapResponse(await requestJson(`/generate/add-http-ts-tool-to-folder?${query}`, {
    method: 'POST',
  }))
}
