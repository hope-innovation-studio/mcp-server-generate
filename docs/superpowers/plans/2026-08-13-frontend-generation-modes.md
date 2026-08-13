# MCP Code Workspace Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace mutually exclusive generation modes with a single code-editor workspace where every Tool owns its generation actions.

**Architecture:** `App.vue` maintains the shared Tool and file-tree state. `ProjectExplorer.vue`, `EditorWorkspace.vue`, and `ToolSidebar.vue` form a persistent three-column layout. Selecting a Tool or file changes only the center editor content.

**Tech Stack:** Vue 3 Composition API, Vite, Fetch API, CSS.

---

### Task 1: Remove mode switching

**Files:**
- Modify: `frontend/src/App.vue`
- Delete: `frontend/src/components/ModeSwitcher.vue`
- Delete: `frontend/src/components/ToolWorkspace.vue`
- Delete: `frontend/src/components/QuickPlayer.vue`
- Delete: `frontend/src/components/ProjectPlayer.vue`

- [x] Remove mutually exclusive quick/project state and components.
- [x] Keep the project tree, editor, and Tool repository visible together.

### Task 2: Build the editor workspace

**Files:**
- Create: `frontend/src/components/ProjectExplorer.vue`
- Create: `frontend/src/components/EditorWorkspace.vue`

- [x] Render the file tree and node creation controls in the left explorer.
- [x] Render Tool definitions in the central editor.
- [x] Put generate-code and add-to-project actions on each Tool.
- [x] Switch the central editor to a node preview when a file-tree node is selected.

### Task 3: Refine visual hierarchy and verify

**Files:**
- Modify: `frontend/src/style.css`

- [x] Replace nested cards with editor panels, separators, and a dark Schema surface.
- [x] Keep neutral glass styling without blue-purple gradients.
- [x] Run `npm run build` successfully.
