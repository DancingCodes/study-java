# 分布式事务 Seata

## 一、为什么需要分布式事务

### 1.1 单体应用的事务

单体应用中，一个 `@Transactional` 就搞定：

```java
@Transactional
public void createOrder(Long productId, Integer quantity) {
    orderMapper.insert(order);       // 写订单表
    productMapper.deductStock(id);   // 扣库存
    // 如果扣库存失败，订单也会回滚 ✓
}
```

### 1.2 微服务的事务问题

微服务拆分后，订单和库存在不同的服务（不同的数据库）：

```
订单服务（订单库）  →  商品服务（商品库）
   insert order         deduct stock
```

问题：
- 订单创建成功了，但扣库存失败了 → 数据不一致
- 两个服务各自的 `@Transactional` 只能管自己的数据库
- 跨服务、跨数据库的事务，本地事务管不了

这就是**分布式事务**问题。

## 二、Seata 简介

Seata 是阿里开源的分布式事务框架，口号是「让分布式事务使用起来像本地事务一样简单」。

### 2.1 核心角色

| 角色 | 说明 |
|------|------|
| **TC（Transaction Coordinator）** | 事务协调者，维护全局事务状态（Seata Server） |
| **TM（Transaction Manager）** | 事务管理者，发起全局事务的一方（通常是调用方） |
| **RM（Resource Manager）** | 资源管理者，管理分支事务（各个参与方） |

```
1. TM（订单服务） → 向 TC 申请开启全局事务
2. TM 调用商品服务扣库存 → RM 注册分支事务
3. 所有操作完成后 → TM 通知 TC 提交/回滚
4. TC → 通知所有 RM 提交/回滚
```

### 2.2 AT 模式（自动模式）

Seata 最常用的模式，对业务代码**零侵入**：

1. **一阶段**：各服务正常执行 SQL，Seata 自动记录「撤销日志」（undo_log）
2. **二阶段-提交**：全部成功 → 删除 undo_log
3. **二阶段-回滚**：有失败 → 根据 undo_log 自动回滚

```
正常情况：
  订单服务 insert → 成功
  商品服务 deduct → 成功
  → TC 通知全部提交 ✓

异常情况：
  订单服务 insert → 成功
  商品服务 deduct → 失败
  → TC 通知全部回滚 → 订单服务根据 undo_log 自动撤销 insert ✓
```

## 三、Seata 安装

### 3.1 下载 Seata Server

下载：https://github.com/apache/incubator-seata/releases

选择最新稳定版（如 2.0.x），解压。

### 3.2 启动

```bash
# Windows
bin\seata-server.bat

# Linux/Mac
sh bin/seata-server.sh
```

默认端口 8091，启动后在 Nacos 服务列表中可以看到 `seata-server`。

### 3.3 各服务数据库建 undo_log 表

AT 模式需要每个参与事务的数据库都有这张表：

```sql
CREATE TABLE IF NOT EXISTS `undo_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `branch_id` BIGINT NOT NULL,
    `xid` VARCHAR(128) NOT NULL,
    `context` VARCHAR(128) NOT NULL,
    `rollback_info` LONGBLOB NOT NULL,
    `log_status` INT NOT NULL,
    `log_created` DATETIME NOT NULL,
    `log_modified` DATETIME NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB;
```

## 四、Spring Boot 整合 Seata

### 4.1 引入依赖

所有参与分布式事务的服务都要引入：

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
</dependency>
```

### 4.2 配置

```yaml
seata:
  tx-service-group: my_tx_group    # 事务组名
  service:
    vgroup-mapping:
      my_tx_group: default         # 映射到 Seata Server 的集群
  registry:
    type: nacos                    # 从 Nacos 发现 Seata Server
    nacos:
      server-addr: localhost:8848
```

### 4.3 使用 @GlobalTransactional

在事务发起方（TM）的方法上加注解，就这么简单：

```java
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductClient productClient;

    @Override
    @GlobalTransactional  // 开启全局事务
    public void createOrder(Long productId, Integer quantity) {
        // 1. 创建订单
        Order order = new Order();
        order.setProductId(productId);
        order.setQuantity(quantity);
        orderMapper.insert(order);

        // 2. 远程调用：扣库存
        productClient.deductStock(productId, quantity);

        // 如果扣库存失败（抛异常），订单也会自动回滚
    }
}
```

商品服务的扣库存接口正常写就行（用本地 `@Transactional`），Seata 自动管理：

```java
@Transactional
public void deductStock(Long productId, Integer quantity) {
    int rows = productMapper.deductStock(productId, quantity);
    if (rows == 0) {
        throw new RuntimeException("库存不足");
    }
}
```

## 五、分布式事务方案对比（了解）

| 方案 | 一致性 | 性能 | 侵入性 | 适用场景 |
|------|--------|------|--------|----------|
| **Seata AT** | 强一致 | 中等 | 零侵入 | 大多数业务场景 |
| **Seata TCC** | 强一致 | 高 | 侵入大（需写 try/confirm/cancel） | 高性能要求 |
| **消息最终一致性** | 最终一致 | 高 | 中等 | 允许短暂不一致（如通知类） |
| **本地消息表** | 最终一致 | 高 | 中等 | 简单场景 |

初学阶段用 Seata AT 模式即可，简单够用。

## 练习

### 练习 1：分布式事务

1. 在订单服务和商品服务的数据库中创建 `undo_log` 表
2. 订单服务引入 Seata 依赖，在 createOrder 方法上加 `@GlobalTransactional`
3. 正常下单 → 验证订单创建成功，库存扣减成功
4. 模拟扣库存失败（库存不足）→ 验证订单也被回滚了

### 练习 2：思考题

1. 本地事务的 `@Transactional` 和分布式事务的 `@GlobalTransactional` 有什么区别？
2. AT 模式的 undo_log 是做什么用的？
3. 什么时候不需要强一致性，可以用消息队列实现最终一致性？
