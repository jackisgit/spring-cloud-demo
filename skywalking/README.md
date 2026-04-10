# SkyWalking 搭建指南

## 一、SkyWalking 简介

SkyWalking 是一个开源的应用性能监控(APM)系统,特别适用于微服务、云原生和容器化的分布式系统。它提供了:

- **服务拓扑图**: 自动生成服务间的调用关系图
- **链路追踪**: 追踪请求在微服务间的完整调用链路
- **性能指标**: 监控服务的响应时间、吞吐量、错误率等
- **告警功能**: 支持多种告警规则和通知方式

## 二、环境要求

- Java 8+
- Spring Boot 2.7.18
- Spring Cloud 2021.0.9
- Spring Cloud Alibaba 2021.0.5.0

## 三、SkyWalking 安装

### 3.1 下载 SkyWalking

```bash
# 下载 SkyWalking APM 8.16.0
wget https://archive.apache.org/dist/skywalking/8.16.0/apache-skywalking-apm-8.16.0.tar.gz

# 解压
tar -zxvf apache-skywalking-apm-8.16.0.tar.gz

# 重命名
mv apache-skywalking-apm-bin skywalking
```

### 3.2 目录结构

```
skywalking/
├── agent/              # Agent 目录
│   ├── config/         # Agent 配置
│   │   └── agent.config
│   ├── plugins/        # 插件目录
│   └── skywalking-agent.jar
├── bin/                # 启动脚本
│   ├── oapService.sh   # OAP Server 启动脚本
│   └── webappService.sh # UI 启动脚本
├── config/             # OAP Server 配置
│   └── application.yml
└── webapp/             # UI 目录
```

### 3.3 启动 SkyWalking

```bash
# 进入 SkyWalking 目录
cd /Users/abao/IdeaProjects/skywalking

# 设置 Java 17 环境(必须使用 Java 11+)
export JAVA_HOME=/Users/abao/Library/Java/JavaVirtualMachines/ms-17.0.18/Contents/Home

# 启动 OAP Server 和 UI
bin/startup.sh
```

### 3.4 访问 UI

打开浏览器访问: http://localhost:8080

默认端口:
- **UI**: 8080
- **OAP Server (gRPC)**: 11800
- **OAP Server (HTTP)**: 12800

## 四、项目集成 SkyWalking

### 4.1 Agent 配置

项目已提供 Agent 配置文件: `skywalking/agent/config/agent.config`

主要配置项:

```properties
# 服务名称(启动时通过 JVM 参数指定)
agent.service_name=${SW_SERVICE_NAME:default-service}

# OAP Server 地址
collector.backend_service=${SW_BACKEND:127.0.0.1:11800}

# 日志级别
logging.level=${SW_LOGGING_LEVEL:INFO}
```

### 4.2 启动服务

#### 方式一: 使用启动脚本

项目已为每个服务创建了启动脚本:

```bash
# Gateway 服务
./skywalking/startup-gateway.sh

# User Service
./skywalking/startup-user-service.sh

# Product Service
./skywalking/startup-product-service.sh

# Order Service
./skywalking/startup-order-service.sh
```

**注意**: 需要修改脚本中的 `SKYWALKING_AGENT_PATH` 为实际的 Agent 路径。

#### 方式二: IDEA 配置

在 IDEA 中配置 VM Options:

```
-javaagent:/Users/abao/IdeaProjects/skywalking-agent/skywalking-agent.jar
-Dskywalking.agent.service_name=gateway
-Dskywalking.collector.backend_service=127.0.0.1:11800
```

各服务的 `service_name`:
- Gateway: `gateway`
- User Service: `user-service`
- Product Service: `product-service`
- Order Service: `order-service`

#### 方式三: Maven 启动

```bash
# 编译项目
mvn clean package -DskipTests

# 启动服务(以 Gateway 为例)
java -javaagent:/Users/abao/IdeaProjects/skywalking-agent/skywalking-agent.jar \
  -Dskywalking.agent.service_name=gateway \
  -Dskywalking.collector.backend_service=127.0.0.1:11800 \
  -jar gateway/target/spring-cloud-demo-gateway.jar
```

## 五、验证集成

### 5.1 启动所有服务

1. 启动 Nacos (端口 8848)
2. 启动 SkyWalking OAP Server 和 UI
3. 启动所有微服务(带 SkyWalking Agent)

### 5.2 访问 SkyWalking UI

打开 http://localhost:8080

### 5.3 查看监控数据

1. **服务拓扑图**: 在 "Topology" 页面可以看到服务间的调用关系
2. **服务列表**: 在 "Service" 页面可以看到所有注册的服务
3. **链路追踪**: 在 "Trace" 页面可以查看请求的完整调用链路
4. **性能指标**: 点击具体服务可以查看响应时间、吞吐量等指标

### 5.4 测试链路追踪

通过 Gateway 发起请求:

```bash
# 用户服务
curl http://localhost:9000/api/users/1

# 产品服务
curl http://localhost:9000/api/products/1

# 订单服务
curl http://localhost:9000/api/orders/1
```

在 SkyWalking UI 的 "Trace" 页面可以看到完整的调用链路。

## 六、高级配置

### 6.1 采样率配置

在 `agent.config` 中配置采样率:

```properties
# 采样率,默认为 -1,表示 100% 采样
# 设置为正整数 N,表示每 3 秒采样 N 个请求
agent.sample_n_per_3_secs=100
```

### 6.2 忽略特定路径

创建 `agent.config` 同级目录下的 `apm-trace-ignore-plugin` 配置:

```properties
# 忽略健康检查等路径
trace.ignore_path=/actuator/health,/actuator/info
```

### 6.3 数据持久化

SkyWalking 默认使用 H2 数据库,生产环境建议使用 Elasticsearch 或 MySQL。

#### 使用 Elasticsearch

修改 `config/application.yml`:

```yaml
storage:
  selector: ${SW_STORAGE:elasticsearch}
  elasticsearch:
    nameSpace: ${SW_NAMESPACE:""}
    clusterNodes: ${SW_STORAGE_ES_CLUSTER_NODES:localhost:9200}
```

#### 使用 MySQL

修改 `config/application.yml`:

```yaml
storage:
  selector: ${SW_STORAGE:mysql}
  mysql:
    properties:
      jdbcUrl: ${SW_JDBC_URL:"jdbc:mysql://localhost:3306/swtest"}
      dataSource.user: ${SW_DATA_SOURCE_USER:root}
      dataSource.password: ${SW_DATA_SOURCE_PASSWORD:root}
```

### 6.4 告警配置

修改 `config/alarm-settings.yml`:

```yaml
rules:
  # 服务响应时间告警
  service_resp_time_rule:
    metrics-name: service_resp_time
    op: ">"
    threshold: 1000
    period: 10
    count: 3
    message: "服务 {name} 平均响应时间超过 1 秒"

  # 服务错误率告警
  service_error_rate_rule:
    metrics-name: service_error_rate
    op: ">"
    threshold: 1
    period: 10
    count: 3
    message: "服务 {name} 错误率超过 1%"
```

## 七、常见问题

### 7.1 Agent 无法连接 OAP Server

**问题**: 服务启动后,SkyWalking UI 中看不到服务

**解决**:
1. 检查 OAP Server 是否启动: `ps -ef | grep oap`
2. 检查端口是否监听: `netstat -an | grep 11800`
3. 检查防火墙是否开放端口
4. 检查 `collector.backend_service` 配置是否正确

### 7.2 链路追踪不完整

**问题**: 只能看到部分服务的链路

**解决**:
1. 确保所有服务都配置了 SkyWalking Agent
2. 检查 Agent 日志: `logs/skywalking-api.log`
3. 确认使用的组件有对应的插件(如 Feign、Gateway 等)

### 7.3 性能影响

**问题**: 担心 SkyWalking 影响服务性能

**解决**:
1. 调整采样率,减少数据采集量
2. 使用异步发送方式
3. 优化 OAP Server 配置
4. 使用 Elasticsearch 作为存储,提高查询性能

### 7.4 Gateway 无法追踪

**问题**: Gateway 服务的链路追踪不生效

**解决**:
确保 Gateway 使用的是 `spring-cloud-gateway` 插件,而不是 `spring-webmvc` 插件。SkyWalking Agent 会自动识别并加载正确的插件。

## 八、最佳实践

1. **生产环境采样**: 建议采样率设置为 10%-30%,避免全量采集影响性能
2. **数据持久化**: 使用 Elasticsearch 作为存储,支持大规模数据
3. **告警配置**: 根据业务需求配置合理的告警规则
4. **命名规范**: 服务名称使用统一命名规范,便于识别和管理
5. **监控大盘**: 结合 Grafana 等工具,构建可视化监控大盘

## 九、参考资料

- [SkyWalking 官方文档](https://skywalking.apache.org/docs/)
- [SkyWalking GitHub](https://github.com/apache/skywalking)
- [Spring Cloud 集成 SkyWalking](https://skywalking.apache.org/docs/skywalking-java-agent/latest/en/setup/service-agent/java-agent/application-toolkit-use-case/spring-cloud/)
