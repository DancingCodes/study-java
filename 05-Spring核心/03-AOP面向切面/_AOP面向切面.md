# AOP 面向切面编程

## 一、什么是 AOP

AOP（Aspect Oriented Programming）= **面向切面编程**。

一句话理解：**在不修改原有代码的情况下，给方法统一添加额外功能。**

比如你有 100 个方法，每个都要加日志记录。传统做法是每个方法里都写一遍日志代码。AOP 的做法是：写一次日志逻辑，自动应用到所有方法上。

## 二、AOP 解决什么问题

假设有一个 UserService：

```java
@Service
public class UserService {
    public void register(String name) {
        long start = System.currentTimeMillis();       // 重复代码
        System.out.println("[日志] register 开始");     // 重复代码

        System.out.println("保存用户：" + name);         // 真正的业务

        long end = System.currentTimeMillis();          // 重复代码
        System.out.println("[日志] register 结束，耗时：" + (end - start) + "ms"); // 重复代码
    }

    public void delete(String name) {
        long start = System.currentTimeMillis();       // 同样的重复代码
        System.out.println("[日志] delete 开始");       // ...

        System.out.println("删除用户：" + name);

        long end = System.currentTimeMillis();
        System.out.println("[日志] delete 结束，耗时：" + (end - start) + "ms");
    }
}
```

**问题**：日志代码和业务代码混在一起，而且每个方法都要写一遍。

**AOP 的解决方式**：把日志逻辑抽出来，做成一个"切面"，自动织入到目标方法上。

## 三、AOP 核心概念

| 概念 | 说明 | 类比 |
|------|------|------|
| **切面（Aspect）** | 你要添加的额外功能（如日志、权限检查） | 一个拦截器 |
| **通知（Advice）** | 切面在什么时机执行（方法前/后/异常时） | 拦截的时机 |
| **切入点（Pointcut）** | 哪些方法需要被拦截 | 拦截的范围 |
| **连接点（JoinPoint）** | 实际被拦截的那个方法 | 具体的某个方法调用 |

简单记：**切面 = 在哪些方法（切入点）的什么时候（通知）做什么事。**

## 四、AOP 的底层原理 — 代理模式

AOP 的本质是**代理**。Spring 不是直接调用你的对象，而是生成一个代理对象包裹你的对象：

```
调用方 → 代理对象 → 你的对象
              ↓
         执行切面逻辑（日志、权限等）
```

Spring 用两种代理：
- **JDK 动态代理** — 目标类有接口时使用
- **CGLIB 代理** — 目标类没有接口时使用（生成子类）

你不需要手写代理，Spring 自动帮你生成，了解有这回事就行。

## 五、Spring AOP 实战

### 5.1 添加依赖

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>6.1.4</version>
</dependency>
```

### 5.2 开启 AOP

在配置类上加 `@EnableAspectJAutoProxy`：

```java
@Configuration
@ComponentScan("com.study")
@EnableAspectJAutoProxy  // 开启 AOP
public class AppConfig {
}
```

### 5.3 五种通知类型

| 注解 | 时机 | 用途 |
|------|------|------|
| `@Before` | 方法执行**前** | 权限检查、参数校验 |
| `@AfterReturning` | 方法正常返回**后** | 记录返回值 |
| `@AfterThrowing` | 方法抛异常**后** | 异常记录 |
| `@After` | 方法执行**后**（无论成功失败） | 清理资源 |
| `@Around` | 包裹整个方法（最强大） | 日志、计时、事务 |

### 5.4 写一个日志切面

```java
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect      // 标记为切面
@Component   // 交给 Spring 管理
public class LogAspect {

    // 切入点：com.study.service 包下所有类的所有方法
    @Pointcut("execution(* com.study.service.*.*(..))")
    public void servicePointcut() {
    }

    // 环绕通知（最常用）
    @Around("servicePointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();

        // 方法执行前
        long start = System.currentTimeMillis();
        System.out.println("[日志] " + methodName + " 开始");

        // 执行目标方法（就是你的业务方法）
        Object result = joinPoint.proceed();

        // 方法执行后
        long end = System.currentTimeMillis();
        System.out.println("[日志] " + methodName + " 结束，耗时：" + (end - start) + "ms");

        return result;
    }
}
```

现在 UserService 可以只写业务代码了：

```java
@Service
public class UserService {
    public void register(String name) {
        System.out.println("保存用户：" + name);
    }

    public void delete(String name) {
        System.out.println("删除用户：" + name);
    }
}
```

运行效果：
```
[日志] register 开始
保存用户：张三
[日志] register 结束，耗时：1ms
```

**UserService 一行日志代码都没写，AOP 自动加上了！**

### 5.5 切入点表达式

```java
// 格式：execution(返回类型 包名.类名.方法名(参数类型))

// 所有 service 包下所有类的所有方法
@Pointcut("execution(* com.study.service.*.*(..))")

// UserService 的所有方法
@Pointcut("execution(* com.study.service.UserService.*(..))")

// 所有 register 方法
@Pointcut("execution(* com.study.service.*.register(..))")

// 所有返回 void 的方法
@Pointcut("execution(void com.study.service.*.*(..))")
```

记住通配符：
- `*` — 匹配一个层级（包名/类名/方法名/返回类型）
- `..` — 匹配任意参数

### 5.6 其他通知示例

```java
@Aspect
@Component
public class LogAspect {

    // 前置通知
    @Before("execution(* com.study.service.*.*(..))")
    public void before() {
        System.out.println("[Before] 方法即将执行");
    }

    // 返回后通知（可以拿到返回值）
    @AfterReturning(value = "execution(* com.study.service.*.*(..))", returning = "result")
    public void afterReturning(Object result) {
        System.out.println("[AfterReturning] 返回值：" + result);
    }

    // 异常后通知（可以拿到异常）
    @AfterThrowing(value = "execution(* com.study.service.*.*(..))", throwing = "ex")
    public void afterThrowing(Exception ex) {
        System.out.println("[AfterThrowing] 异常：" + ex.getMessage());
    }

    // 最终通知（类似 finally，无论是否异常都执行）
    @After("execution(* com.study.service.*.*(..))")
    public void after() {
        System.out.println("[After] 方法执行完毕");
    }
}
```

**实际开发中 90% 用 `@Around`**，因为它最灵活，前后都能拦截。

## 六、AOP 的实际应用场景

| 场景 | 说明 |
|------|------|
| **日志记录** | 记录方法调用、参数、返回值、耗时 |
| **权限校验** | 方法执行前检查用户是否有权限 |
| **事务管理** | Spring 的 `@Transactional` 就是用 AOP 实现的 |
| **接口限流** | 限制接口调用频率 |
| **缓存** | 方法执行前查缓存，有就直接返回 |

其中**事务管理**是最重要的应用，下一课会详细讲。

## 七、自定义注解 + AOP（进阶）

除了用切入点表达式匹配方法，还可以自定义注解来标记哪些方法需要拦截：

```java
// 自定义注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String value() default "";
}

// 切面：拦截所有加了 @Log 的方法
@Aspect
@Component
public class LogAspect {

    @Around("@annotation(log)")  // 匹配加了 @Log 注解的方法
    public Object around(ProceedingJoinPoint joinPoint, Log log) throws Throwable {
        System.out.println("[日志] " + log.value() + " 开始");
        Object result = joinPoint.proceed();
        System.out.println("[日志] " + log.value() + " 结束");
        return result;
    }
}

// 使用：只有加了 @Log 的方法才会被拦截
@Service
public class UserService {

    @Log("用户注册")  // 这个方法会被拦截
    public void register(String name) {
        System.out.println("保存用户：" + name);
    }

    public void delete(String name) {  // 这个不会被拦截
        System.out.println("删除用户：" + name);
    }
}
```

这种方式比切入点表达式更精确，企业开发中非常常用。

## 总结

| 概念 | 说明 |
|------|------|
| AOP | 不修改代码，统一添加额外功能 |
| `@Aspect` | 标记切面类 |
| `@Pointcut` | 定义拦截哪些方法 |
| `@Around` | 最常用的通知，包裹整个方法 |
| `@Before`/`@After` | 方法前/后执行 |
| `@annotation` | 匹配自定义注解 |
| `ProceedingJoinPoint` | 代表被拦截的方法，调用 `proceed()` 执行原方法 |

## 练习

### 练习 1：日志切面

在 `spring-ioc-demo` 项目中（添加 spring-aspects 依赖）：

1. 创建 `aspect/LogAspect.java`
2. 用 `@Around` 实现：打印方法名、执行耗时
3. 切入点设为 `com.study.service` 包下所有方法
4. 运行 App，看 UserService 的方法是否自动打印日志

### 练习 2：自定义注解 + AOP

1. 创建自定义注解 `@Log`（带一个 value 属性）
2. 修改 LogAspect，用 `@annotation(log)` 匹配
3. 在 UserService 的 register 方法上加 `@Log("用户注册")`
4. 运行测试：register 有日志，delete 没日志

### 练习 3：思考题

1. AOP 的底层原理是什么？
2. `@Around` 通知中，如果不调用 `joinPoint.proceed()` 会怎样？
3. Spring 的 `@Transactional` 是用什么技术实现的？
