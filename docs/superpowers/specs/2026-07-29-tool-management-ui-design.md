# Tool 管理页面设计

## 目标

在被寄宿者 Spring Boot 应用中提供 Tool 管理页面。用户可以按通信方式浏览已扫描的 Tool，查看单个 Tool 的完整定义，并为后续生成独立 MCP Server 项目批量选择 Tool。

第一版仅消费现有 HTTP Tool 查询接口；SSE 和 WebSocket 仅提供页面入口与空状态，不伪造数据。

## 页面结构

页面采用浅色 Bento Grid、毛玻璃和柔和阴影风格。

- 顶部栏：产品名称、刷新按钮、进入或退出批量选择模式的按钮。
- 左侧固定边栏：通信方式切换、搜索框、当前通信方式下的 Tool 列表。
- 右侧详情工作区：展示当前选中 Tool 的完整定义，使用 Bento 卡片组织信息。
- 底部批量操作栏：仅在批量选择模式且存在选中项时显示。

左侧边栏的通信方式切换项为 HTTP、SSE、WS。HTTP 项下的单个列表项展示 HTTP 方法和 Tool 名称；SSE、WS 在尚无数据时展示明确空状态。

## 交互规则

### 普通模式

点击左侧 Tool 项会将该项设为当前 Tool，并在右侧详情区展示其信息。列表项只展示摘要，路径、参数和响应不在列表中展开。

### 批量选择模式

用户主动点击“批量选择”后，左侧每个 Tool 项展示选择控件。此时点击列表项只切换选中状态，不切换右侧详情。底部操作栏展示已选数量、取消选择和“生成 MCP 项目”操作。

批量选择限定在当前通信方式下。例如当前处于 HTTP 页签时，只选择 HTTP Tool。

## 数据与 API

第一版前端从现有接口获取 HTTP Tool：

```text
GET /httpDefinitionTool/get
```

前端 API 层将该响应转换为页面所需的展示模型。页面不能直接依赖 Java 反射对象；后端响应应仅包含可序列化字段。

未来后端统一支持以下通信方式：

```text
HTTP      -> HttpToolDefinition
SSE       -> SseToolDefinition
WEBSOCKET -> WebSocketToolDefinition
```

对应的前端通信方式枚举为 `HTTP`、`SSE`、`WEBSOCKET`。HTTP 方法 `GET`、`POST` 等仅属于 HTTP Tool 的摘要信息，不与通信方式混淆。

## Vue 组件边界

```text
frontend/src/
├── api/toolApi.js
├── views/ToolManagementView.vue
└── components/
    ├── AppHeader.vue
    ├── ToolSidebar.vue
    ├── ToolTransportTabs.vue
    ├── ToolSidebarItem.vue
    ├── ToolDetailWorkspace.vue
    ├── EndpointCard.vue
    ├── ParameterCard.vue
    ├── RequestBodyCard.vue
    ├── ResponseCard.vue
    └── BatchActionBar.vue
```

- `toolApi.js`：仅负责请求后端 API。
- `ToolManagementView.vue`：维护当前通信方式、当前详情 Tool、批量选择模式和选中集合。
- `ToolSidebar.vue`：显示和筛选当前通信方式下的摘要列表。
- `ToolDetailWorkspace.vue`：接收一个 Tool 并渲染详情卡片。
- `BatchActionBar.vue`：只接收选中 Tool 和操作回调，不负责筛选或加载数据。

## 详情内容

HTTP Tool 详情区应优先显示：

- Tool 名称和描述。
- 请求方法与完整 endpoint。
- 请求参数：名称、位置、类型、是否必填。
- 请求体格式与 Schema（后端信息具备时）。
- 响应格式与返回 Schema（后端信息具备时）。

## 错误与空状态

- 加载中：左侧显示骨架屏或加载提示。
- 请求失败：显示可重试的错误状态，不清空上一次成功读取的数据。
- HTTP、SSE、WS 没有 Tool：右侧显示对应的空状态说明。
- 初始未选中 Tool：右侧提示“从左侧选择一个 Tool 查看详情”。

## 验证范围

第一版验证以下行为：

1. 页面能从 HTTP 查询接口加载 Tool。
2. 左侧列表仅显示 HTTP 方法和 Tool 名称。
3. 点击普通列表项能更新右侧详情。
4. 切换 SSE、WS 时显示空状态。
5. 进入批量选择模式后，点击列表项只更新选中状态。
6. 退出批量选择模式后，恢复点击查看详情的行为。
