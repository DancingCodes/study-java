# RabbitMQ 消息队列

## 一、为什么需要消息队列

### 1.1 没有消息队列的问题

假设一个下单流程：
```
用户下单 → 扣库存 → 发短信 → 发邮件 → 写日志
```

每一步都是同步调用，总耗时 = 所有步骤耗时之和。如果发短信的服务挂了，整个下单也失败了。

### 1.2 加入消息队列后

```
用户下单 → 扣库存 → 发消息到队列 → 返回"下单成功"
                        ↓
            短信服务、邮件服务、日志服务 各自消费消息
```

**三大好处**：
- **异步**：下单只需要等扣库存，发短信/邮件在后台异步处理，响应更快
- **解耦**：下单服务不直接依赖短信服务，短信挂了不影响下单
- **削峰**：秒杀时 1 万请求涌入，消息队列慢慢消费，数据库不会被打爆

## 二、RabbitMQ 基本概念

### 2.1 核心组件

```
Producer（生产者） → Exchange（交换机） → Queue（队列） → Consumer（消费者）
```

| 概念 | 说明 | 类比 |
|------|------|------|
| **Producer** | 发送消息的程序 | 寄信人 |
| **Exchange** | 接收消息并路由到队列 | 邮局分拣中心 |
| **Queue** | 存储消息的队列 | 信箱 |
| **Consumer** | 接收并处理消息的程序 | 收信人 |
| **Routing Key** | 路由规则，决定消息去哪个队列 | 信封上的地址 |

### 2.2 Exchange 类型

| 类型 | 路由规则 | 场景 |
|------|----------|------|
| **Direct** | 精确匹配 routing key | 点对点，指定某个队列 |
| **Fanout** | 广播，不看 routing key，发给所有绑定的队列 | 广播通知（所有服务都收到） |
| **Topic** | 通配符匹配（`*` 匹配一个词，`#` 匹配多个词） | 按规则分发 |

## 三、RabbitMQ 安装

推荐 Docker 安装：

```bash
# 拉取带管理界面的镜像
docker pull rabbitmq:3-management

# 启动容器
docker run -d --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management
```

- **5672**：应用程序连接端口
- **15672**：管理界面端口
- 浏览器访问 `http://localhost:15672`，账号密码都是 `guest`

## 四、Spring Boot 整合 RabbitMQ

### 4.1 引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

### 4.2 配置连接

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    virtual-host: /
```

### 4.3 定义队列和交换机

```java
@Configuration
public class RabbitConfig {

    // 队列
    @Bean
    public Queue orderQueue() {
        return new Queue("order.queue", true); // true = 持久化
    }

    // 交换机
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange("order.exchange");
    }

    // 绑定：队列绑定到交换机，routing key = "order"
    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with("order");
    }
}
```

### 4.4 生产者 — 发送消息

```java
@Service
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void createOrder(String orderId) {
        // 业务逻辑...
        
        // 发送消息到交换机
        rabbitTemplate.convertAndSend("order.exchange", "order", orderId);
        log.info("订单消息已发送：{}", orderId);
    }
}
```

### 4.5 消费者 — 接收消息

```java
@Component
@Slf4j
public class OrderConsumer {

    @RabbitListener(queues = "order.queue")
    public void handleOrder(String orderId) {
        log.info("收到订单消息：{}", orderId);
        // 发短信、发邮件等后续处理
    }
}
```

`@RabbitListener` 注解标记的方法会自动监听指定队列，有消息就触发。

### 4.6 发送 JSON 对象

实际项目中通常发送对象而不是字符串：

```java
// 配置 JSON 消息转换器
@Bean
public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
}

// 发送对象
rabbitTemplate.convertAndSend("order.exchange", "order", orderDTO);

// 接收对象
@RabbitListener(queues = "order.queue")
public void handleOrder(OrderDTO order) {
    log.info("收到订单：{}", order);
}
```

## 五、Fanout 广播模式

一条消息发给所有绑定的队列（类似微信群消息，所有人都收到）：

```java
@Configuration
public class FanoutConfig {

    @Bean
    public FanoutExchange notifyExchange() {
        return new FanoutExchange("notify.exchange");
    }

    @Bean
    public Queue smsQueue() {
        return new Queue("notify.sms.queue", true);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue("notify.email.queue", true);
    }

    @Bean
    public Binding smsBinding(Queue smsQueue, FanoutExchange notifyExchange) {
        return BindingBuilder.bind(smsQueue).to(notifyExchange);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, FanoutExchange notifyExchange) {
        return BindingBuilder.bind(emailQueue).to(notifyExchange);
    }
}
```

发送消息时 routing key 随便填（Fanout 不看）：
```java
rabbitTemplate.convertAndSend("notify.exchange", "", message);
```

## 六、消息可靠性（了解）

生产环境需要保证消息不丢失：

| 环节 | 问题 | 解决 |
|------|------|------|
| 生产者 → Exchange | 消息没到交换机 | **Confirm 回调** |
| Exchange → Queue | 路由失败 | **Return 回调** |
| Queue 存储 | RabbitMQ 宕机 | **队列持久化 + 消息持久化** |
| Consumer 消费 | 消费者处理失败 | **手动 ACK** |

这些在后续实战项目中再详细实现，这里了解概念即可。

## 七、RabbitMQ vs Kafka

| 特点 | RabbitMQ | Kafka |
|------|----------|-------|
| 模型 | 消息队列（消费后删除） | 日志流（保留一段时间） |
| 吞吐量 | 万级 | 百万级 |
| 延迟 | 微秒级 | 毫秒级 |
| 适用场景 | 业务消息（订单、通知） | 大数据、日志收集、事件流 |
| 学习成本 | 较低 | 较高 |

**选型建议**：中小项目用 RabbitMQ，大数据/日志场景用 Kafka。

## 练习

### 练习 1：简单消息发送与接收

1. 创建 Direct 交换机和队列
2. `POST /order` — 创建订单并发送消息
3. 消费者接收消息并打印日志
4. 观察控制台日志，确认消息被正确消费

### 练习 2：Fanout 广播

1. 创建 Fanout 交换机，绑定短信队列和邮件队列
2. 发送一条通知消息
3. 观察两个消费者都收到了消息

### 练习 3：思考题

1. 消息队列的三大作用是什么？
2. Direct、Fanout、Topic 三种交换机的区别？
3. 如果消费者处理消息失败了，消息会怎样？
