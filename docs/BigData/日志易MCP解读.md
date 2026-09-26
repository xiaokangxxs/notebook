---
blog: true
date: 2026-09-26
description: 从源码看 rizhiyi-mcp 的接入方式、九类能力、请求链路与部署边界。
---

# 日志易MCP解读

`rizhiyi-mcp` 把日志易的查询、分析和管理 API 包装成 MCP 工具，让 AI 客户端能先发现工具，再按用户意图调用。它并不替代日志易的数据存储与计算：真正的日志数据和凭据校验仍在日志易实例一侧。本文依据仓库默认分支 `spec/migrate-mcp-servers-to-python-http` 的固定提交 [`65d7cd1`](https://github.com/rizhiyi/rizhiyi-mcp/tree/65d7cd1da07ce96b9d54b3815e1178b9a8882080) 阅读源码，记录时间为 2026 年 9 月 26 日。

## 架构图

![日志易 MCP 源码层面的逻辑架构图](./日志易MCP解读.svg)

[打开可交互的完整架构图](https://xiaokangxxs.dpdns.org/diagrams/rizhiyi-mcp-architecture.html)。图中的三种入口是可选部署路径，并不表示必须同时运行；三组业务能力共用“工具处理 → 上游 HTTP 客户端 → 日志易 API”这条逻辑链。图由 [Archify](https://github.com/tt-a1i/archify) 按上述固定源码版本制作。

## 从客户端到日志易，调用如何走

1. AI 客户端通过 MCP 的 `tools/list` 发现能力，再发起 `tools/call`。本地可用 TypeScript 的 stdio 进程；远程或多人接入可走 TypeScript Express 网关或 Python FastAPI 网关的 Streamable HTTP。
2. 入口按服务名选择 MCP Server，例如 `/mcp/log-tools`、`/mcp/alert`。TypeScript 和 Python 各有一份注册表，本图将其合并为“服务路由与注册”这个逻辑层。
3. Server 校验工具参数并交给对应业务模块；业务模块通过统一 HTTP 客户端访问日志易 API，然后整理结果返回 MCP 客户端。
4. HTTP 模式从 `Authorization` 请求头取得身份，并把同一会话绑定到同一身份；网关检查凭据格式，凭据是否有效由上游日志易 API 判定。含中文用户名时，客户端会把 `username` 放到上游 URL 查询参数中。

这条链路可在 [TypeScript 注册表](https://github.com/rizhiyi/rizhiyi-mcp/blob/65d7cd1da07ce96b9d54b3815e1178b9a8882080/ts/src/server-registry.ts)、[HTTP 网关](https://github.com/rizhiyi/rizhiyi-mcp/blob/65d7cd1da07ce96b9d54b3815e1178b9a8882080/ts/src/http-server.ts)、[HTTP 客户端](https://github.com/rizhiyi/rizhiyi-mcp/blob/65d7cd1da07ce96b9d54b3815e1178b9a8882080/ts/src/client.ts) 以及对应的 [Python 注册表](https://github.com/rizhiyi/rizhiyi-mcp/blob/65d7cd1da07ce96b9d54b3815e1178b9a8882080/python/rizhiyi_mcp/server_registry.py)、[网关](https://github.com/rizhiyi/rizhiyi-mcp/blob/65d7cd1da07ce96b9d54b3815e1178b9a8882080/python/rizhiyi_mcp/gateway.py) 中核对。

## 九类服务各做什么

| 服务路由 | 作用 | 典型能力 |
| --- | --- | --- |
| `log-tools` | 日志检索与分析 | `log_search_sheet`、趋势摘要、异常点、关联与根因建议 |
| `chatspl` | 自然语言与 SPL 之间的辅助 | 根据描述生成或解释 SPL、查询知识规则 |
| `dashboard` | 仪表盘与面板 | 从模板或规格创建、调整布局、评估展示效果 |
| `parserrule` | 写入时解析规则 | 创建、验证、查询解析规则 |
| `fieldconfig` | 读取时动态字段 | 查看与管理字段配置 |
| `ingest` | 数据接入配置 | Agent 分组、pipeline 等管理 |
| `alert` | 监控告警配置 | 创建、更新、预览、试运行与查询告警 |
| `manage` | 精简的管理 API 导航 | 按模块和 API 选择，再生成调用 |
| `openapi` | 较完整的 API 直通 | 从 OpenAPI/YAML 描述动态生成工具 |

前七类是面向特定场景的工具集合；`manage` 和 `openapi` 更接近配置驱动的 API 入口。前者使用精简的 `config/Api_5.3_schema_mini.yaml`，后者读取完整的 `config/Api_5.3_schema.yaml`。九个路由在 [两版注册表](https://github.com/rizhiyi/rizhiyi-mcp/blob/65d7cd1da07ce96b9d54b3815e1178b9a8882080/python/rizhiyi_mcp/server_registry.py) 中都有登记；“两版均有该服务”不等于每个执行细节都完全一致。

## 大结果为什么返回 `resource_uri`

日志检索很容易产生比一次工具响应适合承载的更多内容。`log-tools` 会按 `result_delivery`、结果大小等条件决定内联返回还是存为共享结果；后者只在工具响应中给出 `resource_uri`、摘要和过期信息。客户端可用 `resources/read` 读取，后续分析工具也能传入同一个 URI 复用结果，减少重复查询和上下文占用。共享结果默认放在本地临时目录，默认保留 30 分钟。这是 MCP 进程侧的暂存机制，不是日志易自身的长期存储。[TypeScript 实现](https://github.com/rizhiyi/rizhiyi-mcp/blob/65d7cd1da07ce96b9d54b3815e1178b9a8882080/ts/src/log-tools-server.ts)、[Python 实现](https://github.com/rizhiyi/rizhiyi-mcp/blob/65d7cd1da07ce96b9d54b3815e1178b9a8882080/python/rizhiyi_mcp/shared_result_store.py)。

## 接入和使用时需要注意的细节

- **以实际工具清单为准。** README 首页有旧工具名，例如 `statistics_analyze`、`trend_analysis`、`anomaly_detect`；当前搜索工具中可见的是 `trend_summary`、`anomaly_points` 等。README 的 HTTP 路径列表还写过 `parserule`，而注册表和示例配置用的是 **`/mcp/parserrule`**。接入时以运行中的 `tools/list`、注册表和工具定义核实名称。[README](https://github.com/rizhiyi/rizhiyi-mcp/blob/65d7cd1da07ce96b9d54b3815e1178b9a8882080/README.md)、[HTTP 配置示例](https://github.com/rizhiyi/rizhiyi-mcp/blob/65d7cd1da07ce96b9d54b3815e1178b9a8882080/mcp-http.json.example)。
- **选择适合的实现。** TypeScript 支持 stdio 与 HTTP，Python 只提供 HTTP；Python 的包要求 Python ≥3.10。两版工具名称基本对齐，但 Python 的 `output_format` 当前统一输出 JSON 文本；TypeScript HTTP 对 MCP GET 请求明确返回 405，而 Python 可接受带会话的 GET 事件流。客户端若依赖特定传输行为，应按所选实现实测。[Python 包配置](https://github.com/rizhiyi/rizhiyi-mcp/blob/65d7cd1da07ce96b9d54b3815e1178b9a8882080/python/pyproject.toml)、[Python 工具定义](https://github.com/rizhiyi/rizhiyi-mcp/blob/65d7cd1da07ce96b9d54b3815e1178b9a8882080/python/rizhiyi_mcp/log_tools_definitions.py)。
- **检查暴露面与资源隔离。** 两版默认监听 `0.0.0.0:3000`，连接上游 HTTPS 时默认不校验证书。公开部署时应明确限制网关访问范围，并设置 `LOGEASE_TLS_REJECT_UNAUTHORIZED=true`。从这一提交的共享结果列表/读取代码看，资源没有按请求用户做所有权过滤；因此多人共用网关前，应隔离实例和结果目录，并用实际账户测试可见性。这是基于源码的风险判断，不是对线上实例的渗透测试结论。[TypeScript 配置](https://github.com/rizhiyi/rizhiyi-mcp/blob/65d7cd1da07ce96b9d54b3815e1178b9a8882080/ts/src/config.ts)、[共享结果存储](https://github.com/rizhiyi/rizhiyi-mcp/blob/65d7cd1da07ce96b9d54b3815e1178b9a8882080/ts/src/shared-result-store.ts)、[Python 资源处理](https://github.com/rizhiyi/rizhiyi-mcp/blob/65d7cd1da07ce96b9d54b3815e1178b9a8882080/python/rizhiyi_mcp/servers.py)。

## 小结

这个项目的价值是把日志易已有能力按任务场景整理成 MCP 工具：AI 客户端负责选择与编排，MCP 层负责协议、参数、会话和结果交付，日志易 API 负责实际业务操作。首次接入可从 `log-tools` 与 `chatspl` 开始，再按需要开放配置类工具；上线前重点检查实际工具名、HTTP 认证、TLS 校验和共享结果隔离。本文基于源码解读，未连接真实日志易实例执行端到端测试。
