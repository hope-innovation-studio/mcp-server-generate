# 最小 Spring Boot 后端设计

## 目标

创建一个可通过 Maven 构建、使用 Java 17 和 Spring Boot 3 的最小 HTTP 后端。

## 架构与组件

- `pom.xml`：引入 Spring Boot 父 POM、`spring-boot-starter-web` 和测试依赖。
- 启动类：使用 `@SpringBootApplication` 启动内嵌 Web 服务器。
- 控制器：暴露 `GET /hello`，返回 JSON 文本 `{"message":"Hello, Spring Boot!"}`。

## 请求流程

客户端访问 `http://localhost:8080/hello`；Spring MVC 将请求路由到控制器，并返回 HTTP 200 与 JSON 响应。

## 异常与范围

采用 Spring Boot 默认错误响应。不增加数据库、认证、配置中心或业务逻辑。

## 验证

先添加 MockMvc 接口测试，确认 `/hello` 返回 HTTP 200 和预期 JSON；随后以 Maven 运行测试并打包验证。
