# Tool Management UI Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a Vue 3 Tool management page that loads HTTP Tool definitions, lists them in a transport-aware sidebar, displays the selected Tool in a Bento-style detail workspace, and supports an explicit batch-selection mode.

**Architecture:** The Java controller exposes a serializable HTTP-only response DTO so the browser never receives Spring or reflection types. The Vue app keeps loading and interaction state in `ToolManagementView.vue`; the sidebar emits selection events while detail cards receive a single selected Tool through props. HTTP is the only live transport in this iteration; SSE and WebSocket are visible empty tabs for the future data model.

**Tech Stack:** Spring Boot 3.5, JUnit 5 + MockMvc, Vue 3, Vite, Vitest, Vue Test Utils, CSS custom properties.

---

## File structure

```text
src/main/java/org/hope/mcpservergenerate/
├── controller/BaseController.java
├── model/http/HttpParameterDefinition.java
└── model/http/response/
    ├── HttpParameterResponse.java
    └── HttpToolDefinitionResponse.java

src/test/java/org/hope/mcpservergenerate/controller/
└── BaseControllerTest.java

frontend/
├── package.json
├── vite.config.js
└── src/
    ├── api/toolApi.js
    ├── api/toolApi.test.js
    ├── components/
    │   ├── AppHeader.vue
    │   ├── ToolTransportTabs.vue
    │   ├── ToolSidebar.vue
    │   ├── ToolSidebarItem.vue
    │   ├── ToolDetailWorkspace.vue
    │   ├── EndpointCard.vue
    │   ├── ParameterCard.vue
    │   ├── RequestBodyCard.vue
    │   ├── ResponseCard.vue
    │   └── BatchActionBar.vue
    ├── views/ToolManagementView.vue
    ├── views/ToolManagementView.test.js
    ├── App.vue
    └── style.css
```

### Task 1: Provide a browser-safe HTTP Tool response

**Files:**
- Create: `src/main/java/org/hope/mcpservergenerate/model/http/response/HttpParameterResponse.java`
- Create: `src/main/java/org/hope/mcpservergenerate/model/http/response/HttpToolDefinitionResponse.java`
- Modify: `src/main/java/org/hope/mcpservergenerate/model/http/HttpParameterDefinition.java`
- Modify: `src/main/java/org/hope/mcpservergenerate/utils/http/HttpParamUtils.java`
- Modify: `src/main/java/org/hope/mcpservergenerate/controller/BaseController.java`
- Test: `src/test/java/org/hope/mcpservergenerate/controller/BaseControllerTest.java`

- [ ] **Step 1: Write the failing JSON contract test**

```java
@WebMvcTest(BaseController.class)
class BaseControllerTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean HttpToolDefinitionContext httpToolDefinitionContext;

    @Test
    void shouldReturnSerializableHttpToolResponses() throws Exception {
        HttpParameterDefinition parameter =
                new HttpParameterDefinition("orderId", "java.lang.String", HttpParameterLocation.PATH, true);
        HttpToolDefinition definition = new HttpToolDefinition();
        definition.setName("queryOrder");
        definition.setEndpoint("/orders/{orderId}");
        definition.setRequestMethod(HttpMethod.GET);
        definition.setParameters(List.of(parameter));
        definition.setReturnType(String.class);
        when(httpToolDefinitionContext.get()).thenReturn(List.of(definition));

        mockMvc.perform(get("/httpDefinitionTool/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].requestMethod").value("GET"))
                .andExpect(jsonPath("$[0].parameters[0].type").value("java.lang.String"))
                .andExpect(jsonPath("$[0].returnType").value("java.lang.String"));
    }
}
```

- [ ] **Step 2: Run the test and verify failure**

Run: `mvn -Dtest=BaseControllerTest test`

Expected: FAIL because `HttpToolDefinition` currently hides `requestMethod` and has no serializable parameter type.

- [ ] **Step 3: Add response records and map in the controller**

```java
public record HttpParameterResponse(
        String key, String type, HttpParameterLocation location, Boolean required
) {
    static HttpParameterResponse from(HttpParameterDefinition value) {
        return new HttpParameterResponse(
                value.getKey(), value.getType(), value.getLocation(), value.getRequired());
    }
}
```

```java
public record HttpToolDefinitionResponse(
        String name, String description, String endpoint, String requestMethod,
        String consumes, String produces, List<HttpParameterResponse> parameters,
        String returnType
) {
    public static HttpToolDefinitionResponse from(HttpToolDefinition value) {
        return new HttpToolDefinitionResponse(
                value.getName(), value.getDescription(), value.getEndpoint(),
                value.getRequestMethod() == null ? null : value.getRequestMethod().name(),
                value.getConsumes(), value.getProduces(),
                value.getParameters().stream().map(HttpParameterResponse::from).toList(),
                value.getReturnType() == null ? null : value.getReturnType().getTypeName());
    }
}
```

Add `String type` to `HttpParameterDefinition` and construct it in `HttpParamUtils` with:

```java
parameter.getParameterizedType().getTypeName()
```

Replace the controller return type with:

```java
@GetMapping("get")
public List<HttpToolDefinitionResponse> get() {
    return httpToolDefinitionContext.get().stream()
            .map(HttpToolDefinitionResponse::from)
            .toList();
}
```

- [ ] **Step 4: Verify the API contract**

Run:

```powershell
mvn -Dtest=BaseControllerTest test
mvn test
```

Expected: PASS. The response has only JSON-safe string, boolean, enum and list values.

- [ ] **Step 5: Commit**

```powershell
git add src/main/java/org/hope/mcpservergenerate/controller/BaseController.java src/main/java/org/hope/mcpservergenerate/model/http src/main/java/org/hope/mcpservergenerate/utils/http/HttpParamUtils.java src/test/java/org/hope/mcpservergenerate/controller/BaseControllerTest.java
git commit -m "feat: expose serializable HTTP tool definitions"
```

### Task 2: Configure Vue testing and the development proxy

**Files:**
- Modify: `frontend/package.json`
- Modify: `frontend/vite.config.js`

- [ ] **Step 1: Add test commands and dependencies**

Add:

```json
{
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview",
    "test": "vitest run",
    "test:watch": "vitest"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^6.0.7",
    "@vue/test-utils": "^2.4.6",
    "happy-dom": "^17.6.3",
    "vite": "^8.1.1",
    "vitest": "^4.0.16"
  }
}
```

- [ ] **Step 2: Configure proxy and test environment**

```js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/httpDefinitionTool': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  test: { environment: 'happy-dom' },
})
```

- [ ] **Step 3: Install dependencies and verify the test runner**

Run:

```powershell
cd frontend
npm install
npm test
```

Expected: Vitest starts successfully; it may report no test files until Task 3 creates one.

- [ ] **Step 4: Commit**

```powershell
git add frontend/package.json frontend/package-lock.json frontend/vite.config.js
git commit -m "build: configure Vue tests and API proxy"
```

### Task 3: Implement and test the HTTP API boundary

**Files:**
- Create: `frontend/src/api/toolApi.js`
- Create: `frontend/src/api/toolApi.test.js`

- [ ] **Step 1: Write failing API tests**

```js
import { afterEach, describe, expect, it, vi } from 'vitest'
import { loadHttpTools } from './toolApi'

afterEach(() => vi.restoreAllMocks())

describe('loadHttpTools', () => {
  it('returns JSON from the HTTP Tool endpoint', async () => {
    const tools = [{ name: 'queryOrder', requestMethod: 'GET' }]
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue({ ok: true, json: async () => tools }))
    await expect(loadHttpTools()).resolves.toEqual(tools)
  })

  it('throws a readable error for a rejected response', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue({ ok: false, status: 500 }))
    await expect(loadHttpTools()).rejects.toThrow('加载 HTTP Tool 失败：500')
  })
})
```

- [ ] **Step 2: Verify failure**

Run: `cd frontend; npm test -- toolApi.test.js`

Expected: FAIL because `toolApi.js` does not exist.

- [ ] **Step 3: Implement the API helper**

```js
const HTTP_TOOL_URL = '/httpDefinitionTool/get'

export async function loadHttpTools() {
  const response = await fetch(HTTP_TOOL_URL)
  if (!response.ok) {
    throw new Error(`加载 HTTP Tool 失败：${response.status}`)
  }
  return response.json()
}
```

- [ ] **Step 4: Verify passing tests**

Run: `cd frontend; npm test -- toolApi.test.js`

Expected: PASS with two tests.

- [ ] **Step 5: Commit**

```powershell
git add frontend/src/api/toolApi.js frontend/src/api/toolApi.test.js
git commit -m "feat: load HTTP tools in Vue client"
```

### Task 4: Implement the transport-aware workspace and its interaction contract

**Files:**
- Create: `frontend/src/views/ToolManagementView.vue`
- Create: `frontend/src/views/ToolManagementView.test.js`
- Create: `frontend/src/components/AppHeader.vue`
- Create: `frontend/src/components/ToolTransportTabs.vue`
- Create: `frontend/src/components/ToolSidebar.vue`
- Create: `frontend/src/components/ToolSidebarItem.vue`
- Create: `frontend/src/components/ToolDetailWorkspace.vue`
- Create: `frontend/src/components/EndpointCard.vue`
- Create: `frontend/src/components/ParameterCard.vue`
- Create: `frontend/src/components/RequestBodyCard.vue`
- Create: `frontend/src/components/ResponseCard.vue`
- Create: `frontend/src/components/BatchActionBar.vue`
- Modify: `frontend/src/App.vue`

- [ ] **Step 1: Write the failing interaction test**

```js
it('opens details normally and only toggles selection in batch mode', async () => {
  vi.mock('../api/toolApi', () => ({
    loadHttpTools: vi.fn().mockResolvedValue([
      { name: 'queryOrder', requestMethod: 'GET', endpoint: '/orders/{orderId}', parameters: [] },
      { name: 'createOrder', requestMethod: 'POST', endpoint: '/orders', parameters: [] },
    ]),
  }))

  const wrapper = mount(ToolManagementView)
  await flushPromises()
  await wrapper.get('[data-tool-name="queryOrder"]').trigger('click')
  expect(wrapper.get('[data-testid="tool-title"]').text()).toBe('queryOrder')

  await wrapper.get('[data-testid="batch-mode-toggle"]').trigger('click')
  await wrapper.get('[data-tool-name="createOrder"]').trigger('click')
  expect(wrapper.get('[data-testid="selected-count"]').text()).toContain('1')
  expect(wrapper.get('[data-testid="tool-title"]').text()).toBe('queryOrder')
})
```

- [ ] **Step 2: Verify failure**

Run: `cd frontend; npm test -- ToolManagementView.test.js`

Expected: FAIL because `ToolManagementView.vue` does not exist.

- [ ] **Step 3: Implement state and component responsibilities**

`ToolManagementView.vue` owns:

```js
const activeTransport = ref('HTTP')
const searchText = ref('')
const toolsByTransport = ref({ HTTP: [], SSE: [], WEBSOCKET: [] })
const selectedTool = ref(null)
const isBatchMode = ref(false)
const selectedToolNames = ref(new Set())
const loading = ref(false)
const loadError = ref('')
```

The click handler must preserve the selected detail Tool in batch mode:

```js
function handleToolClick(tool) {
  if (isBatchMode.value) {
    const next = new Set(selectedToolNames.value)
    next.has(tool.name) ? next.delete(tool.name) : next.add(tool.name)
    selectedToolNames.value = next
    return
  }
  selectedTool.value = tool
}
```

`ToolSidebarItem.vue` renders only the HTTP method and Tool name:

```html
<button :data-tool-name="tool.name" class="tool-sidebar-item">
  <span class="http-method" :class="`method-${tool.requestMethod?.toLowerCase()}`">
    {{ tool.requestMethod }}
  </span>
  <span>{{ tool.name }}</span>
</button>
```

`ToolTransportTabs.vue` renders HTTP, SSE and WS. SSE and WS select their tabs but display “暂未扫描到 SSE Tool” or “暂未扫描到 WebSocket Tool” when their lists are empty.

`ToolDetailWorkspace.vue` displays the initial prompt when no Tool is selected; otherwise it composes endpoint, parameter, request-body and response cards. Its Tool title has `data-testid="tool-title"`.

`BatchActionBar.vue` renders only during batch mode and its count text has `data-testid="selected-count"`.

Replace `App.vue` with:

```vue
<script setup>
import ToolManagementView from './views/ToolManagementView.vue'
</script>

<template>
  <ToolManagementView />
</template>
```

- [ ] **Step 4: Verify interactive behaviour and build**

Run:

```powershell
cd frontend
npm test -- ToolManagementView.test.js
npm run build
```

Expected: PASS and Vite creates `frontend/dist`.

- [ ] **Step 5: Commit**

```powershell
git add frontend/src/App.vue frontend/src/views frontend/src/components
git commit -m "feat: add transport-aware tool management workspace"
```

### Task 5: Apply the approved Bento glass visual system

**Files:**
- Modify: `frontend/src/style.css`
- Modify: all UI components created in Task 4

- [ ] **Step 1: Add visual tokens**

```css
:root {
  --page-bg: #edf3ff;
  --surface: rgba(255, 255, 255, 0.58);
  --surface-strong: rgba(255, 255, 255, 0.8);
  --line: rgba(255, 255, 255, 0.72);
  --text: #172033;
  --muted: #6b7487;
  --blue: #4b7cf5;
  --shadow: 0 20px 55px rgba(67, 88, 126, 0.13);
}
```

- [ ] **Step 2: Implement the page grid**

```css
.tool-workspace {
  display: grid;
  grid-template-columns: minmax(260px, 320px) minmax(0, 1fr);
  min-height: calc(100vh - 116px);
  gap: 18px;
}

.glass-card {
  border: 1px solid var(--line);
  border-radius: 24px;
  background: var(--surface);
  box-shadow: var(--shadow);
  backdrop-filter: blur(22px);
}
```

Below `900px`, stack the sidebar above the detail workspace and make the transport tabs horizontally scrollable. GET is green, POST orange, PUT blue, and DELETE red; every label remains text, not colour-only.

- [ ] **Step 3: Verify visual states manually**

Run: `cd frontend; npm run dev`

Verify: HTTP sidebar items show only method + name; normal click updates detail; SSE/WS show empty states; batch click does not alter detail; a narrow viewport stacks without page-level horizontal scrolling.

- [ ] **Step 4: Run all frontend checks**

Run:

```powershell
cd frontend
npm test
npm run build
```

Expected: PASS with no Vite build error.

- [ ] **Step 5: Commit**

```powershell
git add frontend/src/style.css frontend/src/components frontend/src/views/ToolManagementView.vue
git commit -m "style: apply glass bento design to tool workspace"
```

### Task 6: Package the built page with the starter

**Files:**
- Modify: `frontend/vite.config.js`
- Modify: `pom.xml`
- Modify: `.gitignore`

- [ ] **Step 1: Set the Vite production location and base path**

Add:

```js
base: '/mcp-generator/',
build: {
  outDir: '../src/main/resources/META-INF/resources/mcp-generator',
  emptyOutDir: true,
},
```

- [ ] **Step 2: Ignore generated static assets**

Append:

```gitignore
src/main/resources/META-INF/resources/mcp-generator/
```

- [ ] **Step 3: Run the installed Node/npm toolchain during Maven generate-resources**

Add under `<build><plugins>` in `pom.xml`:

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>exec-maven-plugin</artifactId>
    <version>3.5.0</version>
    <executions>
        <execution>
            <id>build-tool-management-ui</id>
            <phase>generate-resources</phase>
            <goals><goal>exec</goal></goals>
            <configuration>
                <workingDirectory>${project.basedir}/frontend</workingDirectory>
                <executable>npm</executable>
                <arguments><argument>run</argument><argument>build</argument></arguments>
            </configuration>
        </execution>
    </executions>
</plugin>
```

This requires Node.js and `npm install` before the first Maven build; it deliberately does not download a second Node distribution.

- [ ] **Step 4: Verify packaged page assets**

Run:

```powershell
mvn clean package
jar tf target/mcp-server-generate-1.0-SNAPSHOT.jar | Select-String 'META-INF/resources/mcp-generator/index.html'
```

Expected: the JAR contains the generated `index.html`. After launching a host application, `http://localhost:8080/mcp-generator/` displays the Vue page and loads `/httpDefinitionTool/get` from the same origin.

- [ ] **Step 5: Commit**

```powershell
git add pom.xml frontend/vite.config.js .gitignore
git commit -m "build: package tool management UI with starter"
```

## Plan self-review

- Spec coverage: Tasks 1–3 cover the real HTTP data contract and loading. Task 4 covers HTTP/SSE/WS navigation, sidebar details and batch-mode interaction. Task 5 covers the approved Bento glass style, responsive layout and empty states. Task 6 ships the result inside the starter JAR.
- No placeholder scan: SSE and WebSocket intentionally have empty states because their backend scanners do not exist. No fake protocol data is introduced.
- Type consistency: backend sends `requestMethod` and `returnType` as strings; the Vue app consumes only serializable JSON fields.

