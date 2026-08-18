import test from 'node:test'
import assert from 'node:assert/strict'
import { PANEL_LIMITS, clampPanelWidth, resizePanel } from '../src/utils/panelResize.js'

test('allows the compact explorer to shrink to 120 pixels', () => {
  assert.equal(PANEL_LIMITS.left.min, 120)
  assert.equal(PANEL_LIMITS.right.min, 160)
})

test('clamps sidebar width to its allowed range', () => {
  assert.equal(clampPanelWidth(120, 220, 520), 220)
  assert.equal(clampPanelWidth(360, 220, 520), 360)
  assert.equal(clampPanelWidth(700, 220, 520), 520)
})

test('left panel grows when pointer moves right', () => {
  assert.equal(resizePanel({ side: 'left', startWidth: 270, startX: 100, currentX: 160, min: 220, max: 520 }), 330)
})

test('right panel grows when pointer moves left', () => {
  assert.equal(resizePanel({ side: 'right', startWidth: 280, startX: 900, currentX: 840, min: 220, max: 440 }), 340)
})
