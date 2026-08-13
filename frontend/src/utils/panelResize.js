export function clampPanelWidth(width, min, max) {
  return Math.min(max, Math.max(min, width))
}

export function resizePanel({ side, startWidth, startX, currentX, min, max }) {
  const distance = currentX - startX
  const width = side === 'right' ? startWidth - distance : startWidth + distance
  return clampPanelWidth(width, min, max)
}
