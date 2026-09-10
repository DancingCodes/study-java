# Spring 事务管理

## 一、什么是事务（回顾）

阶段三学过数据库事务：一组操作要么全部成功，要么全部失败。

```
转账：张三 -500，李四 +500
→ 两步必须同时成功，不能扣了钱但没加上
```

在数据库里我们用 `START TRANSACTION` + `COMMIT` / `ROLLBACK` 手动管理。

但在 Java 代码里，手动管理事务非常麻烦：

```java
public void transfer(String from, String to, int amount) {
    Connection conn = dataSource.getConnection();
    try {
        conn.setAutoCommit(false);           // 开启事务
        accountDao.decrease(conn, from, amount);
        accountDao.increase(conn, to, amount);
        conn.commit();                        // 提交
    } catch (Exception e) {
        conn.rollback();                      // 回滚
        throw e;
    } finally {
        conn.close();
    }
}
```

每个需要事务的方法都要写一遍 try-catch-rollback，**又臭又长**。

## 二、Spring 事务 — @Transactional

Spring 用 AOP 把事务管理封装了，你只需要加一个注解：

```java
@Service
public class AccountService {

    @Transactional  // 加上这个注解，Spring 自动管理事务
    public void transfer(String from, String to, int amount) {
        accountDao.decrease(from, amount);  // 扣钱
        accountDao.increase(to, amount);    // 加钱
        // 如果这里抛异常，Spring 自动回滚！
    }
}
```

**就这么简单！** Spring 在底层用 AOP 帮你做了：
1. 方法执行前 → 开启事务
2. 方法正常结束 → 提交
3. 方法抛异常 → 回滚

这就是上一课学的 AOP 的最重要应用。

## 三、@Transactional 的原理

```
调用 transfer()
       ↓
  Spring 代理对象（AOP）
       ↓
  开启事务（BEGIN）
       ↓
  执行你的 transfer() 方法
       ↓
  没异常 → COMMIT    有异常 → ROLLBACK
```

本质就是 AOP 的 `@Around`：

```java
// Spring 内部大致是这样的（伪代码）
@Around("@annotation(Transactional)")
public Object transactionAround(ProceedingJoinPoint joinPoint) {
    开启事务();
    try {
        Object result = joinPoint.proceed();  // 执行你的方法
        提交事务();
        return result;
    } catch (Exception e) {
        回滚事务();
        throw e;
    }
}
```

## 四、@Transactional 常用属性

### 4.1 rollbackFor — 指定回滚的异常

**重要**：默认只有 `RuntimeException` 和 `Error` 才回滚，`Exception`（受检异常）不回滚！

```java
// 默认行为：只回滚运行时异常
@Transactional
public void doSomething() {
    throw new RuntimeException("会回滚");
}

@Transactional
public void doSomething() throws IOException {
    throw new IOException("不会回滚！因为是受检异常");
}

// 推荐写法：指定所有异常都回滚
@Transactional(rollbackFor = Exception.class)
public void doSomething() throws Exception {
    // 任何异常都会回滚
}
```

**最佳实践：永远加 `rollbackFor = Exception.class`**，不要用默认值。

### 4.2 readOnly — 只读事务

```java
@Transactional(readOnly = true)  // 告诉数据库这个事务只读，可以优化性能
public List<User> findAll() {
    return userDao.findAll();
}
```

查询方法加 `readOnly = true`，数据库会做优化。

### 4.3 timeout — 超时

```java
@Transactional(timeout = 10)  // 10 秒内没执行完就回滚
public void slowOperation() {
    // ...
}
```

### 4.4 常用属性汇总

| 属性 | 说明 | 推荐值 |
|------|------|--------|
| `rollbackFor` | 哪些异常触发回滚 | `Exception.class`（必加） |
| `readOnly` | 是否只读 | 查询方法设为 true |
| `timeout` | 超时秒数 | 按业务设置 |
| `propagation` | 传播行为（见下文） | 默认即可 |

## 五、事务传播行为

当一个事务方法调用另一个事务方法时，事务怎么处理？

```java
@Service
public class OrderService {
    @Transactional
    public void createOrder() {
        orderDao.save(order);
        logService.saveLog("创建订单");  // 这个方法也有 @Transactional
    }
}

@Service
public class LogService {
    @Transactional
    public void saveLog(String msg) {
        logDao.save(msg);
    }
}
```

`createOrder` 调用 `saveLog`，两个都有事务，怎么办？

### 常用的传播行为

| 传播行为 | 说明 | 场景 |
|---------|------|------|
| `REQUIRED`（默认） | 有事务就加入，没有就新建 | 大部分情况 |
| `REQUIRES_NEW` | 总是新建事务，挂起当前事务 | 日志记录（即使主事务回滚，日志也要保存） |
| `SUPPORTS` | 有事务就加入，没有就不用事务 | 查询方法 |

```java
// 日志保存：即使外层事务回滚，日志也要保存成功
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void saveLog(String msg) {
    logDao.save(msg);
}
```

**90% 的情况用默认的 REQUIRED 就行**，只有特殊场景才改传播行为。

## 六、@Transactional 失效的常见场景

这是面试高频题，也是实际开发中常踩的坑：

### 6.1 同类中方法调用（最常见！）

```java
@Service
public class UserService {

    public void methodA() {
        this.methodB();  // ❌ 事务不生效！
    }

    @Transactional
    public void methodB() {
        // ...
    }
}
```

**原因**：Spring 事务是通过代理实现的。`this.methodB()` 是直接调用，没经过代理对象，所以 AOP 不起作用。

**解决**：从外部调用 methodB，或者注入自己。

### 6.2 方法不是 public

```java
@Transactional
private void doSomething() {  // ❌ private 方法，事务不生效
    // ...
}
```

Spring AOP 默认只能代理 public 方法。

### 6.3 异常被 catch 了

```java
@Transactional
public void doSomething() {
    try {
        dao.update(...);
        int i = 1 / 0;  // 异常
    } catch (Exception e) {
        System.out.println("出错了");  // ❌ 异常被吞了，Spring 看不到，不会回滚
    }
}
```

**解决**：catch 后重新抛出，或者手动标记回滚。

### 6.4 失效场景总结

| 场景 | 原因 | 解决方案 |
|------|------|----------|
| 同类方法调用 | 没经过代理 | 从外部调用 |
| 非 public 方法 | AOP 代理不了 | 改为 public |
| 异常被 catch | Spring 看不到异常 | 重新抛出 |
| 受检异常未配置 | 默认只回滚 RuntimeException | 加 `rollbackFor = Exception.class` |

## 七、实际开发中的最佳实践

```java
// Service 层方法的标准写法
@Service
public class OrderService {
    private final OrderDao orderDao;
    private final AccountDao accountDao;

    public OrderService(OrderDao orderDao, AccountDao accountDao) {
        this.orderDao = orderDao;
        this.accountDao = accountDao;
    }

    // 写操作：加事务 + rollbackFor
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(Order order) {
        orderDao.save(order);
        accountDao.deduct(order.getUserId(), order.getAmount());
    }

    // 查询操作：只读事务
    @Transactional(readOnly = true)
    public Order getOrder(Long id) {
        return orderDao.findById(id);
    }
}
```

记住两条规则：
1. **写操作** → `@Transactional(rollbackFor = Exception.class)`
2. **查询操作** → `@Transactional(readOnly = true)`

## 总结

| 概念 | 说明 |
|------|------|
| `@Transactional` | 声明式事务，加在 Service 方法上 |
| 原理 | AOP 代理，方法前开启事务，正常提交，异常回滚 |
| `rollbackFor` | 必加 `Exception.class` |
| `readOnly` | 查询方法设 true |
| 传播行为 | 默认 REQUIRED，特殊场景用 REQUIRES_NEW |
| 失效场景 | 同类调用、非 public、异常被 catch、受检异常 |

## 练习

### 练习 1：思考题

1. `@Transactional` 底层是用什么技术实现的？
2. 为什么推荐加 `rollbackFor = Exception.class`？不加会怎样？
3. 下面代码的事务会生效吗？为什么？

```java
@Service
public class UserService {
    public void register(String name) {
        this.doRegister(name);
    }

    @Transactional(rollbackFor = Exception.class)
    public void doRegister(String name) {
        userDao.save(name);
        throw new RuntimeException("模拟异常");
    }
}
```

4. 下面代码异常后会回滚吗？为什么？

```java
@Transactional(rollbackFor = Exception.class)
public void doSomething() {
    try {
        userDao.save("张三");
        int i = 1 / 0;
    } catch (Exception e) {
        System.out.println("出错了：" + e.getMessage());
    }
}
```

5. `REQUIRED` 和 `REQUIRES_NEW` 的区别是什么？什么场景用 `REQUIRES_NEW`？
