export const PREVIEW_DEBOUNCE_MS = 800

function pascalCase(value) {
  const normalized = String(value || 'Generated')
    .replace(/[^a-zA-Z0-9]+(.)/g, (_, character) => character.toUpperCase())
  return normalized.charAt(0).toUpperCase() + normalized.slice(1)
}

export function createToolClassName(toolName) {
  const className = pascalCase(toolName).replace(/^[^a-zA-Z_$]+/, '')
  return `${className || 'Generated'}Tool`
}

export function createTemplateVariables(node = {}) {
  const model = node.toolTemplateModel || {}
  return {
    className: model.className || createToolClassName(model.toolName),
    toolName: model.toolName || 'generatedTool',
    description: model.description || '由 MCP Server Generate 创建的 Tool',
    requestMethod: model.requestMethod || 'GET',
    url: model.url || 'http://127.0.0.1:8080/api/example',
    parameters: model.allTsHttpParameter || [],
  }
}

function groupParametersByLocation(parameters) {
  const groups = {}
  for (const parameter of parameters) {
    const location = parameter.location || 'UN_KNOW'
    if (!groups[location]) groups[location] = []
    groups[location].push(parameter)
  }
  return groups
}

export function createPreviewRequest(nodeId, variables) {
  const parameters = (variables.parameters || []).map((parameter) => ({ ...parameter }))

  return {
    nodeId,
    tsHttpToolTemplateModel: {
      className: variables.className || createToolClassName(variables.toolName),
      toolName: variables.toolName,
      description: variables.description,
      requestMethod: variables.requestMethod,
      url: variables.url,
      queryTsHttpParameter: groupParametersByLocation(parameters),
      allTsHttpParameter: parameters,
    },
  }
}
