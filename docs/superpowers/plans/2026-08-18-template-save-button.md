# Template Save Button Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Move the template save action from the request URL bar to a persistent bottom-right action area in the variables pane.

**Architecture:** Keep `saveChanges()` and its state unchanged. Restructure only the variables-pane markup into fixed header/tabs, a scrollable content region, and a bottom action bar; update CSS to make the button black with white text.

**Tech Stack:** Vue 3 single-file components, CSS, Node.js built-in test runner

---

### Task 1: Lock the required layout with a component-source test

**Files:**
- Modify: `frontend/tests/templatePreviewComponent.test.js`

- [ ] **Step 1: Write the failing test**

```js
test('places the save action in a persistent footer instead of the address bar', () => {
  const addressBar = component.match(/<div class="request-address-bar">([\s\S]*?)<\/div>/)?.[1] || ''
  assert.doesNotMatch(addressBar, /preview-save-button/)
  assert.match(component, /class="request-panel-scroll"/)
  assert.match(component, /class="request-save-bar"/)
  assert.match(component, /class="preview-save-button"/)
})
```

- [ ] **Step 2: Run the test and verify it fails**

Run: `node --test frontend/tests/templatePreviewComponent.test.js`

Expected: FAIL because the button is still inside `.request-address-bar` and the new wrappers do not exist.

### Task 2: Move the button and add the persistent action layout

**Files:**
- Modify: `frontend/src/components/TemplateFilePreview.vue`
- Modify: `frontend/src/style.css`

- [ ] **Step 1: Change the component structure**

Remove the save button from `.request-address-bar`, wrap the Tool/parameter content in `.request-panel-scroll`, and append this footer inside the variables pane:

```vue
<footer class="request-save-bar">
  <button
    class="preview-save-button"
    :class="saveState"
    type="button"
    :disabled="saving"
    :title="saveError || '保存当前模板参数'"
    @click="saveChanges"
  >
    {{ saving ? '保存中…' : saveState === 'saved' ? '已保存' : saveState === 'error' ? '保存失败' : '保存' }}
  </button>
</footer>
```

- [ ] **Step 2: Implement the layout and button style**

Use a four-row grid for `.request-builder-pane`, make `.request-panel-scroll` the scrolling area, change the URL bar to two columns, and style the footer button with `background:#1f2521` and `color:#fff`.

- [ ] **Step 3: Run the focused test**

Run: `node --test frontend/tests/templatePreviewComponent.test.js`

Expected: all tests PASS.

- [ ] **Step 4: Run the frontend build**

Run: `npm run build` from `frontend`.

Expected: Vite build completes successfully.
