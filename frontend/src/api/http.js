export async function requestJson(url, options = {}) {
  const response = await fetch(url, options)
  if (!response.ok) {
    throw new Error(`请求失败（HTTP ${response.status}）`)
  }
  return response.json()
}

export function unwrapResponse(payload) {
  if (payload && typeof payload === 'object' && 'code' in payload && 'data' in payload) {
    if (payload.code !== 200) {
      throw new Error(payload.message || '后端返回失败')
    }
    return payload.data
  }
  return payload
}
