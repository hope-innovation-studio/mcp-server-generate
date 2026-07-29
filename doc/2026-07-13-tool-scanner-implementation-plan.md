# Tool Scanner Implementation Plan

**Goal:** Scan a Java class at runtime and produce metadata for every public method marked with `@ExposeMcpTool`.

**Architecture:** `ToolScanner` reads declared public methods by reflection. For each annotated method it creates an immutable `ToolDefinition` with the resolved name, description, and source `Method`. A blank annotation name falls back to the Java method name.

**Tech Stack:** Java 17, JUnit Jupiter, Maven.

---

### Task 1: Define the scanner behavior with a test

**Files:**

- Create: `src/test/java/org/mcpservergenerate/scanner/ToolScannerTest.java`

- [ ] Write a test fixture with one annotated public method and one unannotated public method.
- [ ] Assert that scanning returns only the annotated method, preserves its description, and uses its Java method name when `name` is blank.
- [ ] Run `mvn -Dtest=ToolScannerTest test` and confirm the test cannot pass until scanner support exists.

### Task 2: Add minimal Tool metadata

**Files:**

- Create: `src/main/java/org/mcpservergenerate/scanner/ToolDefinition.java`

- [ ] Create a `record ToolDefinition(String name, String description, Method method)`.

### Task 3: Implement runtime annotation scanning

**Files:**

- Create: `src/main/java/org/mcpservergenerate/scanner/ToolScanner.java`

- [ ] Add `List<ToolDefinition> scan(Class<?> toolContainerType)`.
- [ ] Read public methods from the supplied class.
- [ ] Include only methods annotated with `@ExposeMcpTool`.
- [ ] Use `annotation.name()` when non-blank; otherwise use `Method.getName()`.
- [ ] Run `mvn -Dtest=ToolScannerTest test` and confirm the test passes.
