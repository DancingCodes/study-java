# IoC 与 DI 手写实现

## 一、为什么先手写再学框架

Spring 最核心的思想就是 **IoC（控制反转）** 和 **DI（依赖注入）**。直接学 Spring 的注解和配置，你只会用但不理解原理。先手写一个简易版本，理解了本质再学 Spring，事半功倍。

## 二、没有 IoC 之前的问题

假设有一个用户服务，依赖一个数据库操作类：

```java
public class UserDao {
    public void save(String name) {
        System.out.println("保存用户：" + name);
    }
}

public class UserService {
    // 直接 new 依赖对象
    private UserDao userDao = new UserDao();

    public void register(String name) {
        userDao.save(name);
    }
}

public class Main {
    public static void main(String[] args) {
        UserService service = new UserService();
        service.register("张三");
    }
}
```

**问题在哪？**

1. **耦合太紧** — `UserService` 里直接 `new UserDao()`，如果以后换成 `MysqlUserDao` 或 `MongoUserDao`，要改 `UserService` 的代码
2. **不好测试** — 想测试 `UserService` 就必须连带 `UserDao` 一起跑
3. **对象散乱** — 到处 new，无法统一管理对象的生命周期

## 三、IoC — 控制反转

### 3.1 什么是控制反转

**核心思想：对象不再自己创建依赖，而是由外部（容器）来创建和注入。**

| | 传统方式 | IoC 方式 |
|--|---------|----------|
| 谁创建对象 | 自己 new | 容器创建 |
| 谁管理依赖 | 自己找依赖 | 容器注入 |
| 控制权 | 在代码里 | 在容器里（反转了） |

类比：
- 传统方式 = 你自己做饭（买菜、切菜、炒菜全自己来）
- IoC 方式 = 点外卖（你只管吃，外卖平台负责协调饭店给你送）

"控制"指的是**创建对象和管理依赖的控制权**，"反转"指的是这个控制权从代码转移到了容器。

### 3.2 什么是 DI（依赖注入）

DI 是 IoC 的**实现方式**。容器创建好对象后，自动把依赖"注入"进去。

注入方式有三种：

```java
// 1. 构造器注入（推荐）
public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
}

// 2. Setter 注入
public class UserService {
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}

// 3. 字段注入（Spring 的 @Autowired，后面学）
public class UserService {
    @Autowired
    private UserDao userDao;
}
```

## 四、手写简易 IoC 容器

理解了概念，我们来手写一个最简单的 IoC 容器。

### 4.1 设计思路

容器需要做两件事：
1. **存储对象** — 用一个 Map 来存所有对象（Bean）
2. **自动注入** — 创建对象时，自动把它的依赖注入进去

### 4.2 定义注解

```java
import java.lang.annotation.*;

// 标记一个类需要被容器管理
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
}

// 标记一个字段需要自动注入
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
}
```

### 4.3 实现容器

```java
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SimpleContainer {
    // Bean 仓库：key 是类的 Class 对象，value 是该类的实例
    // 比如：{UserDao.class → new UserDao(), UserService.class → new UserService()}
    private Map<Class<?>, Object> beanMap = new HashMap<>();

    // Class<?>... 是可变参数，可以传入多个 Class
    // 调用示例：register(UserDao.class, UserService.class)
    public void register(Class<?>... classes) throws Exception {

        // ========== 第一步：创建所有对象 ==========
        // 遍历传入的每个类
        for (Class<?> clazz : classes) {

            // 检查这个类上有没有 @Component 注解
            // 有注解 → 需要被容器管理；没注解 → 跳过
            if (clazz.isAnnotationPresent(Component.class)) {

                // 通过反射创建实例，等价于 new UserDao()
                // getDeclaredConstructor() → 获取无参构造器
                // newInstance() → 调用构造器创建对象
                Object instance = clazz.getDeclaredConstructor().newInstance();

                // 存进 Map，比如 beanMap.put(UserDao.class, userDao实例)
                beanMap.put(clazz, instance);
            }
        }

        // ========== 第二步：注入依赖 ==========
        // 遍历 Map 中所有已创建的对象
        for (Object bean : beanMap.values()) {

            // 获取这个对象的所有字段（成员变量）
            // 比如 UserService 有一个字段：private UserDao userDao;
            for (Field field : bean.getClass().getDeclaredFields()) {

                // 检查这个字段上有没有 @Inject 注解
                if (field.isAnnotationPresent(Inject.class)) {

                    // private 字段默认不能访问，设为 true 才能操作
                    field.setAccessible(true);

                    // field.getType() 获取字段的类型，比如 UserDao.class
                    // 然后从 beanMap 中找到这个类型对应的实例
                    Object dependency = beanMap.get(field.getType());

                    // 找到了就注入：把 userDao实例 赋值给 UserService 的 userDao 字段
                    // 等价于：userService.userDao = userDao实例
                    if (dependency != null) {
                        field.set(bean, dependency);
                    }
                }
            }
        }
    }

    // 从容器中获取指定类型的对象
    // 比如 getBean(UserService.class) → 返回之前创建好的 UserService 实例
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) beanMap.get(clazz);
    }
}
```

### 4.4 使用容器

```java
@Component
public class UserDao {
    public void save(String name) {
        System.out.println("保存用户：" + name);
    }
}

@Component
public class UserService {
    @Inject
    private UserDao userDao;  // 不再 new，由容器注入

    public void register(String name) {
        userDao.save(name);
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        // 1. 创建容器
        SimpleContainer container = new SimpleContainer();

        // 2. 注册 Bean
        container.register(UserDao.class, UserService.class);

        // 3. 从容器获取对象（不再自己 new）
        UserService service = container.getBean(UserService.class);
        service.register("张三");
    }
}
// 输出：保存用户：张三
```

### 4.5 发生了什么

```
容器启动：
1. 扫描 @Component → 创建 UserDao 实例、UserService 实例
2. 扫描 @Inject → 发现 UserService 的 userDao 字段需要注入
3. 从 beanMap 找到 UserDao 实例，注入到 UserService 中

使用时：
4. getBean(UserService.class) → 拿到的 UserService 已经注入好了 UserDao
```

## 五、对比：手写版 vs Spring

| | 手写版 | Spring |
|--|--------|--------|
| 注册 Bean | `@Component` | `@Component`（一样！） |
| 依赖注入 | `@Inject` | `@Autowired`（原理相同） |
| 容器 | `SimpleContainer` | `ApplicationContext` |
| 获取 Bean | `container.getBean()` | `context.getBean()` |
| 扫描方式 | 手动传入类 | 自动包扫描 |

**Spring 本质上就是一个超级加强版的 SimpleContainer**，多了自动扫描、AOP、事务管理等功能，但核心思想完全一样。

## 六、IoC 的好处

1. **解耦** — `UserService` 不再依赖具体的 `UserDao` 实现，容器说给什么就用什么
2. **易测试** — 测试时可以注入一个假的（Mock）对象
3. **统一管理** — 所有对象由容器管理，生命周期可控
4. **单例** — 容器里默认一个类只有一个实例（和你到处 new 不同）

## 练习

### 练习 1：手写 IoC 容器

在 `maven-demo` 项目中创建以下文件，运行起来：

1. `Component.java` — @Component 注解
2. `Inject.java` — @Inject 注解
3. `SimpleContainer.java` — 容器实现
4. `UserDao.java` — 数据层，加 @Component
5. `UserService.java` — 业务层，加 @Component，用 @Inject 注入 UserDao
6. `Main.java` — 创建容器，注册 Bean，获取 UserService 并调用

**要求**：不要复制粘贴，对照 md 手敲一遍，确保理解每一行。

### 练习 2：扩展练习

在练习 1 的基础上，新增一个 `OrderDao` 和 `OrderService`：
- `OrderDao` 有一个 `createOrder(String item)` 方法
- `OrderService` 用 `@Inject` 注入 `OrderDao`，有一个 `placeOrder(String item)` 方法
- 在 Main 中注册并测试

### 练习 3：思考题

1. 如果不用 IoC，`UserService` 需要换一个 `UserDao` 实现（比如从 MySQL 换成 MongoDB），要改哪里？用了 IoC 呢？
2. 容器里的对象是什么时候创建的？每次 `getBean` 都会 new 一个新的吗？
3. `@Inject` 注解本身有什么逻辑吗？还是只是一个标记？
