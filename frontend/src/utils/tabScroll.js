export function calculateVisibleScrollLeft({ scrollLeft, viewportWidth, tabLeft, tabWidth }) {
  const viewportRight = scrollLeft + viewportWidth
  const tabRight = tabLeft + tabWidth
  if (tabLeft < scrollLeft) return tabLeft
  if (tabRight > viewportRight) return tabRight - viewportWidth
  return scrollLeft
}
