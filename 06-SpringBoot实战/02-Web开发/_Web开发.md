# Spring Boot Web 开发

## 一、Web 开发的核心 — 写接口

上一节你已经用 `@RestController` + `@GetMapping` 写了一个返回字符串的 Hello 接口。但真正的后端开发，接口要做的事情远不止返回一句话：

- 接收前端传来的参数（用户名、页码、搜索关键词...）
- 处理业务逻辑
- 返回统一格式的 JSON 数据

这一节就围绕这三件事展开。

## 二、@Controller vs @RestController

先搞清楚这两个注解的区别：

| 注解 | 返回值含义 | 适用场景 |
|------|-----------|----------|
| `@Controller` | 返回**视图名**（HTML 页面） | 传统 MVC，前后端不分离 |
| `@RestController` | 返回**数据**（自动转 JSON） | 前后端分离（我们用这个） |

`@RestController` = `@Controller` + `@ResponseBody`

```java
// 前后端分离时代，统一用 @RestController
@RestController
public class UserController {
    
    @GetMapping("/user")
    public User getUser() {
        // 返回对象，Spring Boot 自动转成 JSON
        return new User(1L, "张三", 25);
    }
}
```

浏览器收到的是：
```json
{"id": 1, "name": "张三", "age": 25}
```

Spring Boot 内置了 Jackson 库，自动把 Java 对象转成 JSON，你不需要手动转换。

## 三、请求映射注解

### 3.1 HTTP 方法对应的注解

HTTP 协议定义了不同的请求方法，Spring Boot 提供了对应的注解：

| HTTP 方法 | 注解 | 语义 | 举例 |
|-----------|------|------|------|
| GET | `@GetMapping` | 查询数据 | 获取用户列表、获取单个用户 |
| POST | `@PostMapping` | 新增数据 | 创建用户、提交表单 |
| PUT | `@PutMapping` | 修改数据（全量更新） | 更新用户全部信息 |
| DELETE | `@DeleteMapping` | 删除数据 | 删除用户 |

```java
@RestController
@RequestMapping("/users")  // 类级别的公共前缀
public class UserController {

    @GetMapping          // GET /users — 查询所有用户
    public List<User> list() { ... }

    @GetMapping("/{id}") // GET /users/1 — 查询单个用户
    public User getById(@PathVariable Long id) { ... }

    @PostMapping         // POST /users — 创建用户
    public User create(@RequestBody User user) { ... }

    @PutMapping("/{id}") // PUT /users/1 — 更新用户
    public User update(@PathVariable Long id, @RequestBody User user) { ... }

    @DeleteMapping("/{id}") // DELETE /users/1 — 删除用户
    public void delete(@PathVariable Long id) { ... }
}
```

### 3.2 @RequestMapping

`@RequestMapping` 是上面所有注解的「父注解」，可以放在**类**上定义公共路径前缀：

```java
@RestController
@RequestMapping("/api/v1/users")  // 所有方法的 URL 都以这个开头
public class UserController {
    
    @GetMapping  // 实际路径：GET /api/v1/users
    public List<User> list() { ... }
}
```

## 四、接收请求参数

前端传参数给后端，有四种主要方式，每种对应一个注解。

### 4.1 @RequestParam — 查询参数

用于接收 URL 中 `?key=value` 形式的参数，最常用于 GET 请求的查询条件。

```
GET /users?name=张三&age=25
```

```java
@GetMapping("/users")
public String search(
        @RequestParam String name,              // 必传
        @RequestParam(required = false) Integer age,  // 可选
        @RequestParam(defaultValue = "1") Integer page // 有默认值
) {
    return "搜索：" + name + "，年龄：" + age + "，页码：" + page;
}
```

**注意**：
- 默认 `required = true`，不传会报 400 错误
- `required = false` 时参数可以不传，值为 null
- `defaultValue` 设置默认值后，参数也变成可选的

### 4.2 @PathVariable — 路径参数

用于接收 URL 路径中的参数，常用于获取单个资源。

```
GET /users/1
GET /users/1/orders/5
```

```java
@GetMapping("/users/{id}")
public String getUser(@PathVariable Long id) {
    return "查询用户，ID：" + id;
}

@GetMapping("/users/{userId}/orders/{orderId}")
public String getOrder(
        @PathVariable Long userId,
        @PathVariable Long orderId
) {
    return "用户" + userId + "的订单" + orderId;
}
```

**路径参数 vs 查询参数怎么选？**
- 路径参数：标识唯一资源 → `/users/1`（获取 ID 为 1 的用户）
- 查询参数：筛选/分页/搜索 → `/users?name=张三&page=2`

### 4.3 @RequestBody — 请求体（JSON）

用于接收 POST/PUT 请求中的 JSON 数据，Spring Boot 自动把 JSON 转成 Java 对象。

前端发送：
```json
POST /users
Content-Type: application/json

{"name": "张三", "age": 25, "email": "zhangsan@example.com"}
```

后端接收：
```java
@PostMapping("/users")
public User create(@RequestBody User user) {
    // user.getName() → "张三"
    // user.getAge() → 25
    System.out.println("创建用户：" + user.getName());
    return user;
}
```

对应的实体类：
```java
@Data  // Lombok，自动生成 getter/setter/toString
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
```

**注意**：`@RequestBody` 一个方法里**只能用一次**（HTTP 请求体只有一个）。

### 4.4 @RequestHeader — 请求头

用于接收 HTTP 请求头中的信息，比如 Token：

```java
@GetMapping("/profile")
public String profile(@RequestHeader("Authorization") String token) {
    return "Token：" + token;
}
```

### 4.5 四种传参方式对比

| 注解 | 参数位置 | 典型用途 | 例子 |
|------|---------|---------|------|
| `@RequestParam` | URL ?后面 | 搜索、分页、筛选 | `?name=张三&page=1` |
| `@PathVariable` | URL 路径中 | 定位具体资源 | `/users/1` |
| `@RequestBody` | 请求体（JSON） | 创建/更新数据 | POST 提交用户信息 |
| `@RequestHeader` | 请求头 | Token、语言偏好 | `Authorization: xxx` |

## 五、统一响应格式 — Result

### 5.1 为什么需要统一格式

如果每个接口返回格式不一样，前端写起来会很痛苦：

```json
// 接口 A 返回
{"name": "张三"}

// 接口 B 返回
{"success": true, "data": [...]}

// 接口 C 返回
{"code": 0, "msg": "ok", "result": {...}}
```

前端不得不为每个接口单独处理，这不合理。所以后端要统一所有接口的返回格式：

```json
{
    "code": 200,
    "message": "操作成功",
    "data": { ... }   // 实际数据放这里
}
```

### 5.2 封装 Result 类

```java
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Integer code;    // 状态码：200 成功，其他失败
    private String message;  // 提示信息
    private T data;          // 返回数据（泛型，可以是任何类型）

    // 成功 — 有数据
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 成功 — 无数据
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    // 失败
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}
```

### 5.3 使用 Result 包装返回值

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = new User(id, "张三", 25, "zhangsan@example.com");
        return Result.success(user);
    }

    @PostMapping
    public Result<User> create(@RequestBody User user) {
        user.setId(1L);  // 模拟数据库生成 ID
        return Result.success(user);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        // 模拟删除
        return Result.success();
    }
}
```

前端收到的响应统一是：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": 1,
        "name": "张三",
        "age": 25,
        "email": "zhangsan@example.com"
    }
}
```

### 5.4 泛型 T 的作用

还记得阶段二学的泛型吗？这里就是实际应用：

```java
Result<User>        // data 是 User 对象
Result<List<User>>  // data 是 User 列表
Result<String>      // data 是字符串
Result<Void>        // data 为空（删除操作等）
```

泛型让一个 Result 类适配所有返回类型，不需要为每种数据写一个 Result 类。

## 六、用 Postman / IDEA HTTP Client 测试接口

GET 请求可以直接在浏览器地址栏测试，但 POST/PUT/DELETE 不行（浏览器地址栏只能发 GET）。

### 6.1 IDEA 自带的 HTTP Client

IDEA 内置了 HTTP 测试工具，不用装额外软件：

1. 项目根目录新建文件 `test.http`
2. 写请求：

```http
### 测试 Hello 接口
GET http://localhost:8080/hello

### 查询用户
GET http://localhost:8080/users/1

### 创建用户
POST http://localhost:8080/users
Content-Type: application/json

{"name": "张三", "age": 25, "email": "zhangsan@example.com"}

### 删除用户
DELETE http://localhost:8080/users/1
```

3. 点击每个请求左边的绿色箭头运行

### 6.2 Postman（可选）

如果你更喜欢图形化工具，可以下载 Postman，填入 URL、选择方法、填写参数即可发送请求。

## 七、本节完整示例

把上面讲的串起来，一个完整的用户接口是这样的：

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
```

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}
```

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = new User(id, "张三", 25, "zhangsan@example.com");
        return Result.success(user);
    }

    @GetMapping
    public Result<List<User>> list(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") Integer page
    ) {
        List<User> users = List.of(
                new User(1L, "张三", 25, "zhangsan@example.com"),
                new User(2L, "李四", 30, "lisi@example.com")
        );
        return Result.success(users);
    }

    @PostMapping
    public Result<User> create(@RequestBody User user) {
        user.setId(1L);
        return Result.success(user);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success();
    }
}
```

## 练习

### 练习 1：商品接口 — 基本 CRUD

在现有的 `springboot-demo` 项目中，创建商品管理的完整接口：

1. 创建 `entity/Product.java` 实体类，字段：`id`(Long)、`name`(String)、`price`(Double)、`stock`(Integer)
2. 创建 `common/Result.java` 统一响应类（带泛型）
3. 创建 `controller/ProductController.java`，路径前缀 `/products`，包含：
   - `GET /products/{id}` — 根据 ID 查询商品，返回 `Result<Product>`
   - `GET /products?name=xxx&minPrice=0&maxPrice=100` — 搜索商品（三个参数都可选），返回 `Result<List<Product>>`
   - `POST /products` — 创建商品（接收 JSON），返回 `Result<Product>`
   - `PUT /products/{id}` — 更新商品（路径参数 + JSON 请求体），返回 `Result<Product>`
   - `DELETE /products/{id}` — 删除商品，返回 `Result<Void>`

注意：现在还没有数据库，数据直接用假数据模拟即可（new 对象返回）。

### 练习 2：写 HTTP 测试文件

在项目根目录创建 `test-api.http`，写出测试上面所有接口的 HTTP 请求。

### 练习 3：思考题

1. `@RequestParam` 和 `@PathVariable` 分别适合什么场景？
2. `@RequestBody` 为什么一个方法里只能用一次？
3. `Result<T>` 中泛型 T 的作用是什么？如果不用泛型会怎样？
4. 如果前端发送 POST 请求但忘了加 `Content-Type: application/json` 请求头，会发生什么？
