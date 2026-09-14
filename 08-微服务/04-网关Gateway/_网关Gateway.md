# 网关 Gateway

## 一、为什么需要网关

### 1.1 没有网关的问题

微服务拆分后，前端需要知道每个服务的地址：

```
前端调用用户服务：http://192.168.1.10:8080/user/1
前端调用商品服务：http://192.168.1.11:8081/product/1
前端调用订单服务：http://192.168.1.12:8082/order/1
```

问题：
- 前端要维护多个服务地址
- 每个服务都要单独做鉴权、限流、日志
- 服务地址变了，前端也得改
- 跨域问题每个服务都要处理

### 1.2 有了网关

```
前端只需要知道一个地址：http://api.example.com

前端 → Gateway → 根据路径转发 → 对应的服务
         ↓
   统一鉴权、限流、日志、跨域
```

网关就是所有请求的**统一入口**。

## 二、Spring Cloud Gateway 基本使用

### 2.1 创建网关服务

在 `cloud-demo` 下新建 `gateway` 模块：

```xml
<dependencies>
    <!-- Gateway -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>

    <!-- Nacos 服务发现（网关也要注册到 Nacos） -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>

    <!-- 负载均衡 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-loadbalancer</artifactId>
    </dependency>
</dependencies>
```

**注意**：Gateway 基于 WebFlux（非阻塞），**不能**引入 `spring-boot-starter-web`。

### 2.2 路由配置

```yaml
spring:
  application:
    name: gateway
  cloud:
    nacos:
      server-addr: localhost:8848
    gateway:
      routes:
        - id: product-route
          uri: lb://product-service    # lb:// 表示从 Nacos 负载均衡
          predicates:
            - Path=/product/**         # 路径匹配规则

        - id: order-route
          uri: lb://order-service
          predicates:
            - Path=/order/**

server:
  port: 9000
```

现在前端只需要访问 `http://localhost:9000`：
- `/product/1` → 转发到 product-service
- `/order/1` → 转发到 order-service

### 2.3 路由核心概念

| 概念 | 说明 | 示例 |
|------|------|------|
| **Route（路由）** | 一条转发规则 | id + uri + predicates + filters |
| **Predicate（断言）** | 匹配条件，满足才转发 | Path、Method、Header 等 |
| **Filter（过滤器）** | 请求/响应的处理逻辑 | 加请求头、鉴权、限流 |

## 三、常用断言（Predicate）

```yaml
predicates:
  - Path=/api/**                    # 路径匹配
  - Method=GET,POST                 # 请求方法
  - Header=Authorization, .*        # 请求头存在
  - Query=keyword                   # 查询参数存在
  - After=2024-01-01T00:00:00+08:00 # 时间之后（定时上线）
  - Before=2024-12-31T23:59:59+08:00 # 时间之前
```

多个断言是 **AND** 关系，全部满足才匹配。

## 四、过滤器（Filter）

### 4.1 内置过滤器

```yaml
routes:
  - id: product-route
    uri: lb://product-service
    predicates:
      - Path=/api/product/**
    filters:
      - StripPrefix=1               # 去掉第一层路径：/api/product/1 → /product/1
      - AddRequestHeader=X-Source, gateway  # 加请求头
```

`StripPrefix` 非常常用：前端访问 `/api/product/1`，去掉 `/api` 前缀后转发到商品服务的 `/product/1`。

### 4.2 全局过滤器（鉴权）

```java
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 登录接口放行
        if (path.contains("/login") || path.contains("/register")) {
            return chain.filter(exchange);
        }

        // 检查 Token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token == null || token.isEmpty()) {
            log.warn("未携带 Token，拒绝访问：{}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // TODO: 验证 Token 合法性

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;  // 数字越小优先级越高
    }
}
```

### 4.3 跨域配置

```yaml
spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowed-origins: "*"
            allowed-methods: "*"
            allowed-headers: "*"
```

有了网关，跨域只在网关配一次，后端服务不需要再配。

## 五、完整配置示例

```yaml
spring:
  application:
    name: gateway
  cloud:
    nacos:
      server-addr: localhost:8848
    gateway:
      routes:
        - id: user-route
          uri: lb://user-service
          predicates:
            - Path=/api/user/**
          filters:
            - StripPrefix=1

        - id: product-route
          uri: lb://product-service
          predicates:
            - Path=/api/product/**
          filters:
            - StripPrefix=1

        - id: order-route
          uri: lb://order-service
          predicates:
            - Path=/api/order/**
          filters:
            - StripPrefix=1

      globalcors:
        cors-configurations:
          '[/**]':
            allowed-origins: "*"
            allowed-methods: "*"
            allowed-headers: "*"

server:
  port: 9000
```

前端统一访问 `http://localhost:9000/api/xxx`，网关自动路由到对应服务。

## 练习

### 练习 1：搭建网关

1. 在 cloud-demo 下新建 gateway 模块
2. 配置路由规则，将 `/product/**` 和 `/order/**` 转发到对应服务
3. 通过网关端口（9000）访问商品和订单接口

### 练习 2：路径重写

1. 前端访问 `/api/product/1`，网关去掉 `/api` 前缀后转发到商品服务的 `/product/1`
2. 用 StripPrefix 过滤器实现

### 练习 3：简单鉴权

1. 实现一个全局过滤器，检查请求头中是否携带 Authorization
2. 没有 Token 返回 401，有 Token 放行
3. 登录接口（/login）不需要 Token
