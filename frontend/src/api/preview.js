import { requestJson, unwrapResponse } from './http.js'

export async function previewHttpTsCode(payload) {
  return unwrapResponse(await requestJson('/generate/preview-ts-http-code', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  }))
}

export async function updateHttpTsCode(payload) {
  return unwrapResponse(await requestJson('/generate/update-ts-http-code', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  }))
}
