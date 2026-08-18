import test from 'node:test'
import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'

const component = readFileSync(
  new URL('../src/components/TemplateFilePreview.vue', import.meta.url),
  'utf8',
)

test('uses the immutable parameter id instead of the editable name as the Vue key', () => {
  assert.match(component, /:key="parameter\.id \|\| `\$\{parameter\.location\}:\$\{index\}`"/)
  assert.doesNotMatch(component, /:key="`\$\{parameter\.location\}:\$\{parameter\.key\}`"/)
})

test('offers an explicit save action backed by the update endpoint', () => {
  assert.match(component, /updateHttpTsCode/)
  assert.match(component, /@click="saveChanges"/)
  assert.match(component, /defineEmits\(\['saved'\]\)/)
})

test('places the save action in a persistent footer instead of the address bar', () => {
  const addressBar = component.match(/<div class="request-address-bar">([\s\S]*?)<\/div>/)?.[1] || ''

  assert.doesNotMatch(addressBar, /preview-save-button/)
  assert.match(component, /class="request-panel-scroll"/)
  assert.match(component, /class="request-save-bar"/)
  assert.match(component, /class="preview-save-button"/)
})

test('uses the save button to indicate unsaved template changes', () => {
  assert.match(component, /const savedTemplateSnapshot = ref\(''\)/)
  assert.match(component, /const hasUnsavedChanges = computed/)
  assert.match(component, /class="save-reminder-dot"/)
  assert.match(component, /'保存修改'/)
  assert.match(component, /'已保存'/)
})
