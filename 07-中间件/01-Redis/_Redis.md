# Redis

## 一、为什么需要 Redis

到目前为止，所有数据都存在 MySQL 里。每次请求都查数据库，当并发量大时数据库扛不住。

```
用户请求 → Controller → Service → MySQL（慢，每次都走磁盘）
```

加了 Redis 之后：
```
用户请求 → Controller → Service → 先查 Redis（快，内存读取）
                                    ↓ 没有
                                   查 MySQL → 写入 Redis
```

**Redis 是什么？**
- 基于**内存**的键值（Key-Value）数据库
- 读写速度极快：10 万次/秒（MySQL 大约几千次/秒）
- 常用场景：缓存、Session 存储、排行榜、计数器、分布式锁

类比：MySQL 是图书馆（大量藏书但找起来慢），Redis 是你桌上的便签（容量小但一眼就看到）。

## 二、Redis 安装

### 2.1 Windows 安装

推荐使用 Docker 安装（最简单）：

```bash
# 拉取 Redis 镜像
docker pull redis:7

# 启动 Redis 容器
docker run -d --name redis -p 6379:6379 redis:7
```

如果没装 Docker，也可以下载 Windows 版 Redis：
- 下载地址：https://github.com/tporadowski/redis/releases
- 解压后运行 `redis-server.exe`

### 2.2 验证安装

```bash
# 连接 Redis
redis-cli

# 测试
127.0.0.1:6379> ping
PONG              ← 看到 PONG 就说明连接成功

127.0.0.1:6379> set name "张三"
OK
127.0.0.1:6379> get name
"张三"
```

### 2.3 图形化工具

推荐安装 **Another Redis Desktop Manager**（免费），可以直观地查看和管理 Redis 数据。

## 三、Redis 数据类型

Redis 不只是简单的 key-value，它支持 5 种常用数据类型：

### 3.1 String（字符串）

最基础的类型，一个 key 对应一个值：

```bash
set name "张三"          # 设置
get name                  # 获取 → "张三"
set count 100             # 数字也是 String
incr count                # 自增 → 101
decr count                # 自减 → 100
setex token 3600 "abc123" # 设置并指定过期时间（秒）
ttl token                 # 查看剩余过期时间
```

**典型场景**：缓存、计数器、验证码（设过期时间）

### 3.2 Hash（哈希）

一个 key 对应多个字段，类似 Java 的 Map：

```bash
hset user:1 name "张三"    # 设置字段
hset user:1 age 25
hget user:1 name           # 获取单个字段 → "张三"
hgetall user:1             # 获取所有字段
hdel user:1 age            # 删除字段
```

**典型场景**：存储对象（用户信息、商品信息）

### 3.3 List（列表）

有序列表，可以从两端插入和弹出：

```bash
lpush messages "消息1"     # 左侧插入
lpush messages "消息2"
rpush messages "消息3"     # 右侧插入
lrange messages 0 -1       # 获取全部 → ["消息2", "消息1", "消息3"]
lpop messages              # 左侧弹出
rpop messages              # 右侧弹出
```

**典型场景**：消息队列、最新列表（最近 10 条消息）

### 3.4 Set（集合）

无序、不重复的集合：

```bash
sadd tags "Java"           # 添加
sadd tags "Spring"
sadd tags "Java"           # 重复添加无效
smembers tags              # 获取全部 → ["Java", "Spring"]
sismember tags "Java"      # 是否存在 → 1（true）
srem tags "Spring"         # 删除
```

**典型场景**：标签、点赞用户（去重）、共同好友（交集）

### 3.5 Sorted Set（有序集合）

每个元素带一个分数（score），按分数排序：

```bash
zadd ranking 100 "张三"    # 添加，分数 100
zadd ranking 90 "李四"
zadd ranking 110 "王五"
zrange ranking 0 -1 withscores   # 按分数从低到高
zrevrange ranking 0 -1 withscores # 按分数从高到低
zrank ranking "张三"               # 排名（从 0 开始）
```

**典型场景**：排行榜、热搜

### 3.6 数据类型总结

| 类型 | 对应 Java | 典型场景 |
|------|-----------|----------|
| String | String | 缓存、计数器、验证码 |
| Hash | Map | 存储对象 |
| List | LinkedList | 消息队列、最新列表 |
| Set | HashSet | 标签、去重、交集运算 |
| Sorted Set | TreeMap | 排行榜 |

## 四、Key 过期与淘汰

### 4.1 设置过期时间

```bash
set token "abc123"
expire token 3600          # 设置 3600 秒后过期
ttl token                  # 查看剩余时间

setex code 300 "123456"    # 设置值同时设置过期时间（验证码 5 分钟）
```

过期后 key 自动删除，不需要手动清理。

### 4.2 缓存常见问题

| 问题 | 描述 | 解决方案 |
|------|------|----------|
| **缓存穿透** | 查询不存在的数据，每次都打到数据库 | 缓存空值（设短过期时间） |
| **缓存击穿** | 热点 key 过期瞬间，大量请求打到数据库 | 互斥锁、热点 key 永不过期 |
| **缓存雪崩** | 大量 key 同时过期，数据库压力暴增 | 过期时间加随机值、多级缓存 |

了解即可，后面实战项目中会遇到。

## 五、Spring Boot 整合 Redis

### 5.1 引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 5.2 配置 Redis 连接

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      # password: 123456    # 有密码就加上
      database: 0            # 使用第 0 号数据库（默认）
```

### 5.3 配置 RedisTemplate

Spring Boot 默认的 `RedisTemplate` 序列化方式不好用（键值是乱码），需要自定义配置：

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // key 用 String 序列化
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // value 用 JSON 序列化
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
```

### 5.4 使用 RedisTemplate

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // String 操作
    public void setString() {
        redisTemplate.opsForValue().set("name", "张三");
        redisTemplate.opsForValue().set("token", "abc123", 30, TimeUnit.MINUTES); // 带过期
        String name = (String) redisTemplate.opsForValue().get("name");
    }

    // Hash 操作
    public void setHash() {
        redisTemplate.opsForHash().put("user:1", "name", "张三");
        redisTemplate.opsForHash().put("user:1", "age", 25);
        Object name = redisTemplate.opsForHash().get("user:1", "name");
    }

    // 删除
    public void delete() {
        redisTemplate.delete("name");
    }

    // 判断是否存在
    public void exists() {
        Boolean hasKey = redisTemplate.hasKey("name");
    }
}
```

### 5.5 常用 API 对照

| Redis 命令 | RedisTemplate 方法 |
|-----------|--------------------|
| `set key value` | `opsForValue().set(key, value)` |
| `get key` | `opsForValue().get(key)` |
| `setex key seconds value` | `opsForValue().set(key, value, timeout, unit)` |
| `del key` | `delete(key)` |
| `exists key` | `hasKey(key)` |
| `expire key seconds` | `expire(key, timeout, unit)` |
| `hset key field value` | `opsForHash().put(key, field, value)` |
| `hget key field` | `opsForHash().get(key, field)` |
| `lpush key value` | `opsForList().leftPush(key, value)` |
| `sadd key value` | `opsForSet().add(key, value)` |
| `zadd key score value` | `opsForZSet().add(key, value, score)` |

## 六、实战：商品缓存

最典型的 Redis 使用场景 — 查询缓存：

```java
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String PRODUCT_KEY_PREFIX = "product:";

    @Override
    public Product getProductById(Long id) {
        String key = PRODUCT_KEY_PREFIX + id;

        // 1. 先查 Redis
        Product product = (Product) redisTemplate.opsForValue().get(key);
        if (product != null) {
            return product;  // 缓存命中，直接返回
        }

        // 2. 缓存没有，查数据库
        product = productMapper.selectById(id);
        if (product == null) {
            // 缓存空值，防止缓存穿透（过期时间短一些）
            redisTemplate.opsForValue().set(key, null, 5, TimeUnit.MINUTES);
            return null;
        }

        // 3. 写入缓存（30 分钟过期）
        redisTemplate.opsForValue().set(key, product, 30, TimeUnit.MINUTES);
        return product;
    }

    @Override
    public void updateProduct(Product product) {
        // 更新数据库
        productMapper.updateById(product);
        // 删除缓存（下次查询时重新加载）
        redisTemplate.delete(PRODUCT_KEY_PREFIX + product.getId());
    }
}
```

**缓存策略**：
- 查询：先查 Redis → 没有就查 MySQL → 写入 Redis
- 更新/删除：先更新 MySQL → 再删除 Redis 缓存

## 七、分布式锁（了解）

当多个服务实例同时操作同一个资源时，需要分布式锁来保证只有一个实例在执行：

```java
public void deductStock(Long productId) {
    String lockKey = "lock:product:" + productId;

    // 尝试加锁（SET NX EX，原子操作）
    Boolean locked = redisTemplate.opsForValue()
            .setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);

    if (Boolean.TRUE.equals(locked)) {
        try {
            // 执行扣库存逻辑
        } finally {
            // 释放锁
            redisTemplate.delete(lockKey);
        }
    } else {
        throw new BusinessException(429, "操作太频繁，请稍后重试");
    }
}
```

实际项目推荐用 **Redisson** 框架来实现分布式锁（更安全、功能更强），这里只做了解。

## 练习

### 练习 1：Spring Boot 整合 Redis

1. 安装 Redis（推荐 Docker 方式）
2. 创建项目，配置 Redis 连接和 RedisTemplate
3. 创建 `controller/RedisController.java`，实现以下接口：
   - `POST /redis/string?key=xxx&value=xxx` — 设置字符串
   - `GET /redis/string?key=xxx` — 获取字符串
   - `DELETE /redis/string?key=xxx` — 删除
   - `POST /redis/hash?key=xxx&field=xxx&value=xxx` — 设置 Hash 字段
   - `GET /redis/hash?key=xxx&field=xxx` — 获取 Hash 字段
4. 启动后用 test-api.http 测试，同时用 Redis 图形化工具查看数据

### 练习 2：商品查询缓存

1. 在商品查询接口中加入 Redis 缓存逻辑
2. 第一次查询走数据库（控制台有 SQL 日志），第二次查询走缓存（无 SQL 日志）
3. 更新商品后删除缓存，再次查询应该重新走数据库

### 练习 3：思考题

1. Redis 为什么这么快？（内存 + 单线程 + IO 多路复用）
2. 缓存穿透、缓存击穿、缓存雪崩分别是什么？
3. 更新数据时为什么要「先更新数据库，再删除缓存」，而不是「先删缓存，再更新数据库」？
