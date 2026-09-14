# 熔断降级 Sentinel

## 一、为什么需要熔断降级

### 1.1 雪崩效应

```
用户请求 → 订单服务 → 商品服务 → 数据库
```

如果商品服务变慢了（数据库卡住），会发生什么？

1. 订单服务调用商品服务，等待响应...
2. 订单服务的线程被占满（都在等商品服务）
3. 新的请求进来，没有空闲线程处理
4. 订单服务也变慢了
5. 调用订单服务的其他服务也变慢...
6. **整个系统崩溃** → 雪崩效应

### 1.2 解决方案

| 方案 | 说明 |
|------|------|
| **熔断** | 检测到目标服务故障率过高，直接断开调用，不再请求（像电路保险丝） |
| **降级** | 熔断后返回一个兜底结果（如默认值、友好提示），而不是报错 |
| **限流** | 控制请求速率，超过阈值直接拒绝，保护服务不被打爆 |

## 二、Sentinel 简介

Sentinel 是阿里开源的流量控制组件，核心功能：
- **流量控制**（限流）：QPS 限制、线程数限制
- **熔断降级**：慢调用、异常比例、异常数
- **系统保护**：CPU 使用率、总 QPS 等系统级保护

### 2.1 安装 Dashboard

下载：https://github.com/alibaba/Sentinel/releases（选 `sentinel-dashboard-x.x.x.jar`）

启动：
```bash
java -jar sentinel-dashboard-1.8.7.jar --server.port=8090
```

浏览器访问 http://localhost:8090，账号密码都是 `sentinel`。

## 三、Spring Boot 整合 Sentinel

### 3.1 引入依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
```

### 3.2 配置

```yaml
spring:
  cloud:
    sentinel:
      transport:
        dashboard: localhost:8090  # Sentinel Dashboard 地址
        port: 8719                 # 与 Dashboard 通信的端口
```

启动服务后，随便调一个接口，Sentinel Dashboard 就能看到这个服务了（懒加载，不调接口不显示）。

## 四、流量控制（限流）

### 4.1 在 Dashboard 配置

1. 在 Sentinel Dashboard 找到目标接口
2. 点「流控」→ 设置 QPS 阈值（如 5）
3. 超过 5 次/秒的请求直接被拒绝

### 4.2 代码方式

```java
@GetMapping("/product/{id}")
@SentinelResource(value = "getProduct", blockHandler = "getProductBlockHandler")
public Result<Product> getProduct(@PathVariable Long id) {
    return Result.success(productService.getById(id));
}

// 被限流时的兜底方法（参数必须一致，多一个 BlockException）
public Result<Product> getProductBlockHandler(@PathVariable Long id, BlockException e) {
    return Result.error(429, "请求太频繁，请稍后重试");
}
```

### 4.3 限流策略

| 策略 | 说明 |
|------|------|
| 直接 | 超过阈值直接限流 |
| 关联 | A 接口流量大时，限制 B 接口（如写多了限制读） |
| 链路 | 只统计从指定入口进来的流量 |

## 五、熔断降级

### 5.1 熔断策略

| 策略 | 说明 | 示例 |
|------|------|------|
| **慢调用比例** | 响应时间超过阈值的请求占比过高 | 1 秒内 50% 请求超过 500ms |
| **异常比例** | 异常请求占比过高 | 1 秒内 50% 请求异常 |
| **异常数** | 异常请求数超过阈值 | 1 分钟内超过 5 个异常 |

### 5.2 熔断状态

```
正常 → [触发熔断] → 熔断开启（所有请求直接失败）
                         ↓ 等待熔断时长
                    半开状态（放一个请求试探）
                    ↓ 成功             ↓ 失败
                  恢复正常          继续熔断
```

### 5.3 OpenFeign 整合 Sentinel

配置开启：

```yaml
spring:
  cloud:
    openfeign:
      sentinel:
        enabled: true
```

定义 Fallback（降级兜底）：

```java
@FeignClient(name = "product-service", fallbackFactory = ProductClientFallback.class)
public interface ProductClient {

    @GetMapping("/product/{id}")
    Product getById(@PathVariable Long id);
}
```

```java
@Component
@Slf4j
public class ProductClientFallback implements FallbackFactory<ProductClient> {

    @Override
    public ProductClient create(Throwable cause) {
        log.error("商品服务调用失败：{}", cause.getMessage());
        return new ProductClient() {
            @Override
            public Product getById(Long id) {
                // 返回一个兜底的默认商品（或 null）
                Product product = new Product();
                product.setName("商品服务暂时不可用");
                return product;
            }
        };
    }
}
```

这样商品服务挂了，订单服务不会跟着挂，而是返回一个友好的兜底结果。

## 六、Sentinel vs Hystrix

| 特点 | Sentinel | Hystrix |
|------|----------|--------|
| 状态 | 活跃维护 | 已停止维护 |
| 控制台 | 有 Dashboard，规则可视化 | 无独立控制台 |
| 限流 | 支持 | 不支持 |
| 规则管理 | 支持动态规则 | 需要代码配置 |

Spring Cloud Alibaba 体系推荐 Sentinel。

## 练习

### 练习 1：限流

1. 在 Sentinel Dashboard 给商品查询接口配置 QPS = 2 的限流规则
2. 用快速连续请求测试，观察超过阈值后返回限流提示

### 练习 2：熔断降级

1. 给 OpenFeign 的 ProductClient 配置 FallbackFactory
2. 停掉 product-service，调用订单接口，观察是否返回兜底结果（而不是 500 报错）
3. 重启 product-service，观察熔断恢复

### 练习 3：思考题

1. 熔断的三种状态是什么？状态之间怎么切换？
2. 限流和熔断的区别是什么？
3. 为什么 FallbackFactory 比 Fallback 更好用？
