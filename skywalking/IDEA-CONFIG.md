# IDEA 启动配置说明

## 一、配置 VM Options

在 IDEA 中为每个微服务配置 SkyWalking Agent:

### 1. Gateway 服务

**Run/Debug Configurations** -> **Spring Boot** -> **gateway** -> **VM options**:

```
-javaagent:/Users/abao/IdeaProjects/skywalking-agent/skywalking-agent.jar
-Dskywalking.agent.service_name=gateway
-Dskywalking.collector.backend_service=127.0.0.1:11800
```

### 2. User Service

**Run/Debug Configurations** -> **Spring Boot** -> **user-service** -> **VM options**:

```
-javaagent:/Users/abao/IdeaProjects/skywalking-agent/skywalking-agent.jar
-Dskywalking.agent.service_name=user-service
-Dskywalking.collector.backend_service=127.0.0.1:11800
```

### 3. Product Service

**Run/Debug Configurations** -> **Spring Boot** -> **product-service** -> **VM options**:

```
-javaagent:/Users/abao/IdeaProjects/skywalking-agent/skywalking-agent.jar
-Dskywalking.agent.service_name=product-service
-Dskywalking.collector.backend_service=127.0.0.1:11800
```

### 4. Order Service

**Run/Debug Configurations** -> **Spring Boot** -> **order-service** -> **VM options**:

```
-javaagent:/Users/abao/IdeaProjects/skywalking-agent/skywalking-agent.jar
-Dskywalking.agent.service_name=order-service
-Dskywalking.collector.backend_service=127.0.0.1:11800
```

## 二、配置步骤

1. 在 IDEA 中打开 **Run/Debug Configurations**
2. 选择对应的 Spring Boot 应用
3. 在 **VM options** 字段中添加上述配置
4. 点击 **Apply** 和 **OK**

## 三、启动顺序

1. 启动 Nacos (端口 8848)
2. 启动 SkyWalking OAP Server 和 UI
3. 启动各个微服务(按顺序):
   - Gateway
   - User Service
   - Product Service
   - Order Service

## 四、验证集成

1. 访问 SkyWalking UI: http://localhost:8080
2. 在 **Service** 页面查看是否显示所有服务
3. 在 **Topology** 页面查看服务拓扑图
4. 发起请求测试链路追踪

## 五、测试请求

```bash
# 通过 Gateway 访问各个服务
curl http://localhost:9000/api/users/1
curl http://localhost:9000/api/products/1
curl http://localhost:9000/api/orders/1
```

然后在 SkyWalking UI 的 **Trace** 页面查看完整的调用链路。

## 六、常见问题

### 问题1: 服务在 SkyWalking UI 中不显示

**解决方案**:
- 检查 SkyWalking OAP Server 是否启动
- 检查端口 11800 是否可访问
- 检查 Agent 配置是否正确
- 查看服务日志是否有错误

### 问题2: 链路追踪不完整

**解决方案**:
- 确保所有服务都配置了 Agent
- 检查是否使用了支持的组件(Feign、Gateway 等)
- 查看 Agent 日志: `/Users/abao/IdeaProjects/skywalking-agent/logs/skywalking-api.log`

### 问题3: Agent 启动失败

**解决方案**:
- 检查 Java 版本(需要 Java 8+)
- 检查 Agent 路径是否正确
- 查看 IDEA 控制台错误信息

## 七、高级配置

### 采样率配置

在 VM options 中添加:

```
-Dskywalking.agent.sample_n_per_3_secs=100
```

表示每 3 秒采样 100 个请求。

### 忽略特定路径

在 Agent 配置文件中添加:

```properties
agent.ignore_suffix=.jpg,.jpeg,.js,.css,.png,.bmp,.gif,.ico,.mp3,.mp4,.html,.svg,.actuator
```

### 自定义服务名称

可以通过环境变量设置:

```
-Dskywalking.agent.service_name=my-custom-name
```
