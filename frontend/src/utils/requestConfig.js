const LOCATION_GROUPS = {
  PATH: 'params',
  PARAM: 'params',
  MATRIX: 'params',
  UN_KNOW: 'params',
  BODY: 'body',
  MULTIPART: 'body',
  MODEL_ATTRIBUTE: 'body',
  HEADER: 'headers',
  COOKIE: 'cookies',
}

export function groupRequestParameters(parameters = []) {
  const groups = {
    params: [],
    body: [],
    headers: [],
    cookies: [],
  }

  for (const parameter of parameters) {
    const groupName = LOCATION_GROUPS[parameter.location] || 'params'
    groups[groupName].push(parameter)
  }

  return groups
}
