const HTTP_METHODS = ['get', 'post', 'put', 'patch', 'delete']

export function openApiToHttpTools(openApi) {
  return Object.entries(openApi.paths ?? {}).flatMap(([endpoint, pathItem]) => {
    const pathParameters = pathItem.parameters ?? []

    return HTTP_METHODS
      .filter((method) => pathItem[method])
      .map((method) => {
        const operation = pathItem[method]

        return {
          id: `${method.toUpperCase()}:${endpoint}`,
          transport: 'HTTP',
          name:
            operation['x-mcp-tool-name'] ??
            operation.operationId ??
            `${method.toUpperCase()} ${endpoint}`,
          description:
            operation['x-mcp-tool-description'] ??
            operation.description ??
            operation.summary ??
            '',
          tags: operation.tags ?? [],
          endpoint,
          requestMethod: method.toUpperCase(),
          parameters: mergeParameters(pathParameters, operation.parameters ?? []),
          requestBody: getRequestBody(operation),
          response: getSuccessResponse(operation.responses),
        }
      })
  })
}

function mergeParameters(pathParameters, operationParameters) {
  const parameterMap = new Map()

  for (const parameter of [...pathParameters, ...operationParameters]) {
    parameterMap.set(`${parameter.in}:${parameter.name}`, parameter)
  }

  return [...parameterMap.values()]
}

function getRequestBody(operation) {
  const contentEntries = Object.entries(operation.requestBody?.content ?? {})
  const [mediaType, content] = contentEntries[0] ?? []

  if (!mediaType) {
    return null
  }

  return {
    mediaType,
    schema: content.schema ?? null,
  }
}

function getSuccessResponse(responses = {}) {
  const statusCode =
    Object.keys(responses).find((code) => code.startsWith('2')) ??
    Object.keys(responses)[0]

  if (!statusCode) {
    return null
  }

  const response = responses[statusCode]
  const [mediaType, content] = Object.entries(response.content ?? {})[0] ?? []

  return {
    statusCode,
    description: response.description ?? '',
    mediaType: mediaType ?? null,
    schema: content?.schema ?? null,
  }
}
