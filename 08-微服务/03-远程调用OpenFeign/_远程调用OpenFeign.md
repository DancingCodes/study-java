# 远程调用 OpenFeign

## 一、服务间调用方式

### 1.1 RestTemplate（原始方式）

```java
@Autowired
private RestTemplate restTemplate;

public Product getProduct(Long id) {
    String url = "http://product-service/product/" + id;
    return restTemplate.getForObject(url, Product.class);
}
```

问题：URL 拼接麻烦，参数多了代码又臭又长，没有代码提示。

### 1.2 OpenFeign（声明式）

```java
@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/product/{id}")
    Product getById(@PathVariable Long id);
}
```

像调用本地方法一样调用远程服务，OpenFeign 自动处理 HTTP 请求、序列化、负载均衡。

## 二、OpenFeign 基本使用

### 2.1 引入依赖

在调用方（order-service）引入：

```xml
<!-- OpenFeign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<!-- 负载均衡（必须引入，否则报错） -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

### 2.2 启用 OpenFeign

在启动类上加 `@EnableFeignClients`：

```java
@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
```

### 2.3 定义 Feign 接口

```java
@FeignClient(name = "product-service")  // 目标服务名（Nacos 中注册的名字）
public interface ProductClient {

    @GetMapping("/product/{id}")
    Product getById(@PathVariable Long id);

    @GetMapping("/product")
    List<Product> listAll();
}
```

- `name` 对应 Nacos 中的服务名
- 方法签名和目标服务的 Controller 保持一致
- OpenFeign 会自动从 Nacos 获取服务地址，并做负载均衡

### 2.4 调用远程服务

```java
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductClient productClient;

    @Override
    public Order createOrder(Long productId, Integer quantity) {
        // 远程调用商品服务（像调本地方法一样）
        Product product = productClient.getById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        Order order = new Order();
        order.setProductId(productId);
        order.setProductName(product.getName());
        order.setPrice(product.getPrice());
        order.setQuantity(quantity);
        order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        // 保存订单...
        return order;
    }
}
```

## 三、OpenFeign 负载均衡

当 product-service 有多个实例时，OpenFeign 配合 LoadBalancer 自动轮询：

```
order-service
    ↓ 调用 product-service
    ↓ Nacos 返回实例列表：[8081, 8083]
    ↓ LoadBalancer 轮询选择
    → 第1次请求 → 8081
    → 第2次请求 → 8083
    → 第3次请求 → 8081
    → ...
```

不需要额外配置，引入 `loadbalancer` 依赖后自动生效。

## 四、OpenFeign 常用配置

### 4.1 超时配置

```yaml
spring:
  cloud:
    openfeign:
      client:
        config:
          default:                # 全局配置
            connect-timeout: 5000  # 连接超时 5 秒
            read-timeout: 10000    # 读取超时 10 秒
          product-service:        # 针对某个服务单独配置
            read-timeout: 3000
```

### 4.2 日志配置

开发时可以打印 Feign 请求详情（生产环境关掉）：

```yaml
logging:
  level:
    com.study.orderservice.client: DEBUG  # Feign 接口所在包
```

```java
@Bean
public Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;  // 打印请求头、请求体、响应
}
```

### 4.3 请求参数传递

| 参数类型 | Feign 写法 | 示例 |
|----------|-----------|------|
| 路径参数 | `@PathVariable` | `/product/{id}` |
| 查询参数 | `@RequestParam` | `?name=xxx` |
| 请求体 | `@RequestBody` | POST JSON |
| 请求头 | `@RequestHeader` | 传递 Token 等 |

```java
@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/product/{id}")
    Product getById(@PathVariable Long id);

    @GetMapping("/product")
    List<Product> search(@RequestParam String keyword,
                         @RequestParam(required = false) Double maxPrice);

    @PostMapping("/product")
    Product save(@RequestBody Product product);
}
```

## 五、Feign 接口抽取（最佳实践）

多个服务都要调用商品服务时，每个服务都写一遍 ProductClient 太重复。

**最佳实践**：把 Feign 接口放到公共模块（common）：

```
cloud-demo/
├── common/
│   └── client/ProductClient.java    ← 公共 Feign 接口
├── product-service/
└── order-service/                    ← 引入 common 依赖即可使用
```

注意：使用公共模块的 Feign 接口时，需要指定扫描路径：

```java
@EnableFeignClients(basePackages = "com.study.common.client")
```

## 练习

### 练习 1：服务间调用

1. 在 product-service 中创建 `GET /product/{id}` 接口
2. 在 order-service 中定义 ProductClient
3. 创建 `POST /order` 接口，接收 productId 和 quantity，远程调用商品服务获取商品信息，组装订单返回
4. 测试：先调商品接口确认有数据，再调订单接口看是否能拿到商品信息

### 练习 2：负载均衡验证

1. 启动两个 product-service 实例（8081、8083），在返回结果中加上端口号
2. 多次调用订单接口，观察返回的端口号是否交替变化

### 练习 3：思考题

1. OpenFeign 和 RestTemplate 相比有什么优势？
2. 如果被调用的服务挂了，OpenFeign 会怎样？（引出下一课 Sentinel）
