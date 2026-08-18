import test from 'node:test'
import assert from 'node:assert/strict'
import { groupRequestParameters } from '../src/utils/requestConfig.js'

test('groups HTTP parameters into Apifox-style request tabs', () => {
  const groups = groupRequestParameters([
    { key: 'id', location: 'PATH' },
    { key: 'keyword', location: 'PARAM' },
    { key: 'legacy', location: 'UN_KNOW' },
    { key: 'payload', location: 'BODY' },
    { key: 'file', location: 'MULTIPART' },
    { key: 'token', location: 'HEADER' },
    { key: 'session', location: 'COOKIE' },
  ])

  assert.deepEqual(groups.params.map((item) => item.key), ['id', 'keyword', 'legacy'])
  assert.deepEqual(groups.body.map((item) => item.key), ['payload', 'file'])
  assert.deepEqual(groups.headers.map((item) => item.key), ['token'])
  assert.deepEqual(groups.cookies.map((item) => item.key), ['session'])
})

test('returns empty arrays when a request has no parameters', () => {
  assert.deepEqual(groupRequestParameters(), {
    params: [],
    body: [],
    headers: [],
    cookies: [],
  })
})
