# Spring IoC 容器

## 一、从手写到 Spring

上一课我们手写了一个简易 IoC 容器，用 `@Component` + `@Inject` + 反射实现了对象管理和依赖注入。Spring 做的事情完全一样，只是功能更强大：

| 手写版 | Spring |
|--------|--------|
| `@Component` | `@Component`（一样） |
| `@Inject` | `@Autowired` |
| `SimpleContainer` | `ApplicationContext` |
| 手动传入类 | 自动包扫描 `@ComponentScan` |
| 只能字段注入 | 构造器/Setter/字段注入都支持 |
| 每次都创建新对象 | 默认单例，还支持多种作用域 |

## 二、Spring 项目搭建

### 2.1 添加依赖

在 Maven 项目的 `pom.xml` 中添加 Spring Context 依赖：

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>6.1.4</version>
</dependency>
```

这一个依赖就包含了 IoC 容器所需的一切。

### 2.2 项目结构

```
src/main/java/com/study/
├── App.java              ← 启动类
├── dao/
│   └── UserDao.java      ← 数据层
└── service/
    └── UserService.java  ← 业务层
```

## 三、核心注解

### 3.1 @Component — 注册 Bean

加了 `@Component` 的类，Spring 会自动创建它的实例并放到容器里。

```java
import org.springframework.stereotype.Component;

@Component
public class UserDao {
    public void save(String name) {
        System.out.println("保存用户：" + name);
    }
}
```

Spring 还提供了三个语义更明确的变体，**功能完全一样**，只是名字不同方便区分层次：

| 注解 | 用在哪一层 | 说明 |
|------|-----------|------|
| `@Component` | 通用 | 不确定用哪个就用这个 |
| `@Repository` | Dao 层 | 数据访问层 |
| `@Service` | Service 层 | 业务逻辑层 |
| `@Controller` | Controller 层 | 控制器层（Web 请求入口） |

```java
@Repository  // 代替 @Component，语义更清晰
public class UserDao { ... }

@Service     // 代替 @Component
public class UserService { ... }
```

### 3.2 @Autowired — 依赖注入

告诉 Spring：这个字段需要自动注入。

```java
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {
    @Autowired              // Spring 自动从容器中找到 UserDao 实例注入
    private UserDao userDao;

    public void register(String name) {
        userDao.save(name);
    }
}
```

### 3.3 @Autowired 的三种注入方式

```java
// 方式 1：字段注入（最简单，但不推荐用在正式项目）
@Service
public class UserService {
    @Autowired
    private UserDao userDao;
}

// 方式 2：构造器注入（推荐！Spring 官方推荐的方式）
@Service
public class UserService {
    private final UserDao userDao;

    @Autowired  // 构造器只有一个时，@Autowired 可以省略
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
}

// 方式 3：Setter 注入（不常用）
@Service
public class UserService {
    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
```

**为什么推荐构造器注入？**
- 字段可以用 `final`，保证不会被改
- 依赖关系一目了然（看构造器就知道）
- 方便写单元测试（直接 new 传参就行）

### 3.4 @Qualifier — 指定注入哪个

如果一个接口有多个实现类，Spring 不知道注入哪个，用 `@Qualifier` 指定：

```java
public interface MessageSender {
    void send(String msg);
}

@Component("emailSender")
public class EmailSender implements MessageSender {
    public void send(String msg) {
        System.out.println("邮件发送：" + msg);
    }
}

@Component("smsSender")
public class SmsSender implements MessageSender {
    public void send(String msg) {
        System.out.println("短信发送：" + msg);
    }
}

@Service
public class NotifyService {
    @Autowired
    @Qualifier("emailSender")  // 指定注入 emailSender
    private MessageSender sender;
}
```

## 四、Bean 的生命周期

Spring 管理的对象叫 **Bean**。一个 Bean 从创建到销毁的过程：

```
实例化（new）→ 属性注入（@Autowired）→ 初始化（@PostConstruct）→ 使用 → 销毁（@PreDestroy）
```

```java
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class MyBean {

    @PostConstruct  // 对象创建并注入完成后执行（初始化）
    public void init() {
        System.out.println("Bean 初始化");
    }

    @PreDestroy     // 容器关闭前执行（清理资源）
    public void destroy() {
        System.out.println("Bean 销毁");
    }
}
```

常见用途：
- `@PostConstruct` — 加载缓存、初始化连接池、启动定时任务
- `@PreDestroy` — 关闭连接、释放资源

## 五、Bean 的作用域

| 作用域 | 说明 | 使用场景 |
|--------|------|----------|
| `singleton`（默认） | 容器中只有一个实例 | 绝大多数情况 |
| `prototype` | 每次获取都创建新实例 | 有状态的对象 |

```java
import org.springframework.context.annotation.Scope;

@Component
@Scope("prototype")  // 每次 getBean 都创建新对象
public class MyBean { }
```

**99% 的情况用默认的 singleton 就行**，不需要手动设置。

## 六、@ComponentScan — 自动扫描

手写版需要手动注册每个类，Spring 用 `@ComponentScan` 自动扫描指定包下所有带 `@Component` 的类：

```java
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.study")  // 扫描 com.study 包及其子包
public class AppConfig {
}
```

## 七、启动 Spring 容器

```java
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        // 1. 创建 Spring 容器（传入配置类）
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        // 2. 从容器获取 Bean
        UserService userService = context.getBean(UserService.class);
        userService.register("张三");

        // 3. 关闭容器
        context.close();
    }
}
```

对比手写版：
```java
// 手写版
SimpleContainer container = new SimpleContainer();
container.register(UserDao.class, UserService.class);
UserService service = container.getBean(UserService.class);

// Spring 版
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
UserService service = context.getBean(UserService.class);
```

几乎一模一样，只是 Spring 不需要手动注册，自动扫描就行。

## 八、@Value — 注入配置值

除了注入对象，还可以注入配置文件中的值：

```properties
# src/main/resources/application.properties
app.name=我的应用
app.version=1.0
```

```java
import org.springframework.beans.factory.annotation.Value;

@Component
public class AppInfo {
    @Value("${app.name}")       // 从配置文件读取
    private String appName;

    @Value("${app.version}")
    private String version;

    @Value("${app.timeout:30}")  // 冒号后面是默认值，配置里没有就用 30
    private int timeout;
}
```

## 总结

| 概念 | 说明 |
|------|------|
| `@Component` / `@Service` / `@Repository` | 把类交给 Spring 管理 |
| `@Autowired` | 自动注入依赖 |
| `@Qualifier` | 多个实现时指定注入哪个 |
| `@ComponentScan` | 自动扫描包 |
| `@Configuration` | 标记配置类 |
| `@Value` | 注入配置值 |
| `@PostConstruct` / `@PreDestroy` | 生命周期回调 |
| `@Scope` | 设置作用域（singleton/prototype） |

## 练习

### 练习 1：搭建 Spring 项目

在 maven-demo 项目中操作（需要先在 pom.xml 添加 spring-context 依赖）：

1. 创建 `AppConfig.java` — 配置类，加 `@Configuration` 和 `@ComponentScan("com.study")`
2. 创建 `dao/UserDao.java` — 加 `@Repository`，有 `save(String name)` 方法
3. 创建 `service/UserService.java` — 加 `@Service`，用构造器注入 `UserDao`
4. 修改 `App.java` — 创建 Spring 容器，获取 UserService 并调用

运行 App，确认输出 `保存用户：张三`。

### 练习 2：多实现注入

1. 创建 `MessageSender` 接口，有 `send(String msg)` 方法
2. 创建 `EmailSender` 和 `SmsSender` 两个实现类，都加 `@Component`
3. 创建 `NotifyService`，用 `@Autowired` + `@Qualifier` 注入 `EmailSender`
4. 在 App 中测试

### 练习 3：思考题

1. `@Component` 和 `@Service` 功能上有区别吗？为什么还要分开？
2. 为什么 Spring 官方推荐构造器注入而不是字段注入？
3. 默认的 singleton 作用域意味着什么？两次 `getBean` 拿到的是同一个对象吗？
