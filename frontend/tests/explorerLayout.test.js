import test from 'node:test'
import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'

const css = readFileSync(new URL('../src/style.css', import.meta.url), 'utf8')

test('wraps explorer actions below the title when the sidebar is narrow', () => {
  assert.match(
    css,
    /@container \(max-width:220px\)[^{]*\{[^}]*\.explorer-panel \.side-panel-header\{[^}]*flex-wrap:wrap[^}]*\}[^}]*\.explorer-panel \.explorer-header-actions\{[^}]*width:100%[^}]*justify-content:flex-end/s,
  )
})
