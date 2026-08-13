import test from 'node:test'
import assert from 'node:assert/strict'
import { calculateVisibleScrollLeft } from '../src/utils/tabScroll.js'

test('keeps scroll position when active tab is already visible', () => {
  assert.equal(calculateVisibleScrollLeft({
    scrollLeft: 100,
    viewportWidth: 500,
    tabLeft: 180,
    tabWidth: 120,
  }), 100)
})

test('scrolls right to reveal a tab outside the viewport', () => {
  assert.equal(calculateVisibleScrollLeft({
    scrollLeft: 100,
    viewportWidth: 500,
    tabLeft: 560,
    tabWidth: 120,
  }), 180)
})

test('scrolls left to reveal a tab before the viewport', () => {
  assert.equal(calculateVisibleScrollLeft({
    scrollLeft: 300,
    viewportWidth: 500,
    tabLeft: 120,
    tabWidth: 100,
  }), 120)
})
