# 数据库集成 MyBatis Plus

## 一、为什么用 MyBatis Plus

上一节写接口时，数据都是 `new` 出来的假数据。真正的项目数据存在数据库里，需要一个工具帮你在 Java 代码和数据库之间搭桥。

### 1.1 ORM 是什么

ORM（Object Relational Mapping，对象关系映射）= **Java 对象 ↔ 数据库表** 的自动转换。

```
Java 对象          数据库表
Product.java  ←→  product 表
  id               id
  name             name
  price            price
  stock            stock
```

你操作 Java 对象，ORM 框架帮你生成 SQL 并执行。不需要手写大量 SQL。

### 1.2 MyBatis vs MyBatis Plus

| | MyBatis | MyBatis Plus |
|---|---------|-------------|
| 定位 | 半自动 ORM | MyBatis 的增强版 |
| CRUD | 每个 SQL 都要手写 | 单表 CRUD **零 SQL** |
| 分页 | 手写分页 SQL | 内置分页插件 |
| 条件查询 | 手写 WHERE 条件 | 条件构造器，链式调用 |
| 代码生成 | 无 | 一键生成 Entity/Mapper/Service |

MyBatis Plus = MyBatis + 自动 CRUD + 分页 + 条件构造器。**只增强不改变**，你仍然可以在需要时手写 SQL。

类比：MyBatis 是手动挡汽车，MyBatis Plus 是在手动挡上加了自动挡模式。日常开自动挡，复杂路况切手动挡。

## 二、项目搭建

### 2.1 数据库准备

先在 MySQL 中创建数据库和表：

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS study_db DEFAULT CHARSET utf8mb4;

USE study_db;

-- 创建商品表
CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）'
) COMMENT '商品表';

-- 插入测试数据
INSERT INTO product (name, price, stock) VALUES
('iPhone 15', 5999.00, 100),
('小米14', 3999.00, 200),
('华为Mate60', 6999.00, 50),
('MacBook Pro', 12999.00, 30),
('机械键盘', 299.00, 500),
('无线鼠标', 99.00, 1000);
```

注意几个设计要点：
- `id` 用 `BIGINT` + `AUTO_INCREMENT`，对应 Java 的 `Long`
- `price` 用 `DECIMAL(10,2)`，不用 `DOUBLE`（避免精度问题）
- `create_time` / `update_time` 自动记录时间
- `deleted` 用于逻辑删除（后面会讲）

### 2.2 pom.xml 依赖

在 Spring Boot 项目中引入 MyBatis Plus 和 MySQL 驱动：

```xml
<!-- MyBatis Plus -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    <version>3.5.5</version>
</dependency>

<!-- MySQL 驱动 -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

注意：Spring Boot 3.x 要用 `mybatis-plus-spring-boot3-starter`（带 boot3），不是老版本的 `mybatis-plus-boot-starter`。

### 2.3 application.yml 配置

```yaml
server:
  port: 8080

spring:
  application:
    name: mybatis-plus-demo
  datasource:
    url: jdbc:mysql://localhost:3306/study_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl  # 打印 SQL 日志（开发时开启）
    map-underscore-to-camel-case: true  # 下划线自动转驼峰（create_time → createTime）
  global-config:
    db-config:
      logic-delete-field: deleted       # 逻辑删除字段名
      logic-delete-value: 1             # 已删除的值
      logic-not-delete-value: 0         # 未删除的值
```

**重要**：`password` 要改成你自己 MySQL 的密码。

## 三、核心三层结构

MyBatis Plus 的代码组织遵循三层结构：

```
Entity（实体类）  — 对应数据库表，一行数据 = 一个对象
    ↕
Mapper（数据访问层）  — 操作数据库，执行 SQL
    ↕
Service（业务层）  — 业务逻辑，调用 Mapper
    ↕
Controller（控制层）  — 接收请求，调用 Service
```

### 3.1 Entity — 实体类

```java
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")  // 对应数据库表名
public class Product {

    @TableId(type = IdType.AUTO)  // 主键，自增
    private Long id;

    private String name;

    private BigDecimal price;  // 金额用 BigDecimal，不用 Double

    private Integer stock;

    private LocalDateTime createTime;  // 自动映射 create_time（下划线转驼峰）

    private LocalDateTime updateTime;

    @TableLogic  // 逻辑删除标记
    private Integer deleted;
}
```

**关键注解**：
- `@TableName("product")` — 告诉 MyBatis Plus 这个类对应哪张表
- `@TableId(type = IdType.AUTO)` — 标记主键，`AUTO` 表示数据库自增
- `@TableLogic` — 逻辑删除。调用删除方法时不会真正删除数据，而是把 `deleted` 设为 1

**为什么 price 用 BigDecimal 不用 Double？**

```java
System.out.println(0.1 + 0.2);  // 输出 0.30000000000000004 ← Double 精度丢失！
```

涉及到钱的字段，永远用 `BigDecimal`。这是上一节用 `Double` 的升级版。

### 3.2 Mapper — 数据访问层

```java
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    // 不需要写任何方法！BaseMapper 已经提供了全部单表 CRUD
}
```

继承 `BaseMapper<Product>` 后，你自动获得这些方法：

| 方法 | 作用 |
|------|------|
| `insert(entity)` | 插入一条 |
| `deleteById(id)` | 根据 ID 删除（逻辑删除） |
| `updateById(entity)` | 根据 ID 更新 |
| `selectById(id)` | 根据 ID 查询 |
| `selectList(wrapper)` | 条件查询 |
| `selectPage(page, wrapper)` | 分页查询 |

**零 SQL，全自动。**

### 3.3 Service — 业务层

MyBatis Plus 提供了 `IService` 接口和 `ServiceImpl` 实现类，封装了更丰富的方法：

```java
// Service 接口
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProductService extends IService<Product> {
    // 自定义业务方法写在这里
}
```

```java
// Service 实现类
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    // IService 的方法已经全部实现了，这里只写自定义业务逻辑
}
```

`IService` 比 `BaseMapper` 多了一些便捷方法：

| 方法 | 作用 |
|------|------|
| `save(entity)` | 插入 |
| `removeById(id)` | 删除 |
| `updateById(entity)` | 更新 |
| `getById(id)` | 根据 ID 查询 |
| `list()` | 查询全部 |
| `page(page, wrapper)` | 分页查询 |
| `saveBatch(list)` | 批量插入 |
| `count()` | 总数 |

### 3.4 Controller — 调用 Service

```java
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return Result.success(product);
    }

    @GetMapping
    public Result<List<Product>> list() {
        List<Product> products = productService.list();
        return Result.success(products);
    }

    @PostMapping
    public Result<Product> create(@RequestBody Product product) {
        productService.save(product);
        return Result.success(product);  // save 后 id 会自动回填
    }

    @PutMapping("/{id}")
    public Result<Product> update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        productService.updateById(product);
        return Result.success(product);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.removeById(id);
        return Result.success();
    }
}
```

对比上一节的假数据版本，区别就是把 `new Product(...)` 换成了 `productService.xxx()`，数据来自真实数据库。

## 四、条件构造器 — QueryWrapper

查询经常需要加条件（按名称搜索、价格范围、排序等），MyBatis Plus 用 `QueryWrapper` 构建条件：

### 4.1 基本用法

```java
// 查询价格大于 1000 的商品
QueryWrapper<Product> wrapper = new QueryWrapper<>();
wrapper.gt("price", 1000);
List<Product> products = productService.list(wrapper);
```

### 4.2 常用条件方法

| 方法 | SQL | 例子 |
|------|-----|------|
| `eq("name", "手机")` | `name = '手机'` | 等于 |
| `ne("name", "手机")` | `name != '手机'` | 不等于 |
| `gt("price", 1000)` | `price > 1000` | 大于 |
| `ge("price", 1000)` | `price >= 1000` | 大于等于 |
| `lt("price", 5000)` | `price < 5000` | 小于 |
| `le("price", 5000)` | `price <= 5000` | 小于等于 |
| `between("price", 1000, 5000)` | `price BETWEEN 1000 AND 5000` | 范围 |
| `like("name", "手机")` | `name LIKE '%手机%'` | 模糊查询 |
| `orderByAsc("price")` | `ORDER BY price ASC` | 升序 |
| `orderByDesc("price")` | `ORDER BY price DESC` | 降序 |

### 4.3 LambdaQueryWrapper（推荐）

`QueryWrapper` 用字符串写字段名，容易写错。`LambdaQueryWrapper` 用方法引用，编译时就能检查：

```java
// 字符串方式 — 写错了 "proce" 编译不报错，运行才报错
QueryWrapper<Product> wrapper = new QueryWrapper<>();
wrapper.gt("proce", 1000);  // ← 字段名写错了，不会报编译错误

// Lambda 方式 — 写错了编译直接报错（推荐！）
LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
wrapper.gt(Product::getPrice, 1000);  // ← 用方法引用，安全
```

### 4.4 条件组合实战

商品搜索接口：按名称模糊查询 + 价格范围 + 按价格排序

```java
@GetMapping("/search")
public Result<List<Product>> search(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice
) {
    LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();

    // 条件不为空时才加入查询条件
    wrapper.like(name != null, Product::getName, name)
           .ge(minPrice != null, Product::getPrice, minPrice)
           .le(maxPrice != null, Product::getPrice, maxPrice)
           .orderByAsc(Product::getPrice);

    List<Product> products = productService.list(wrapper);
    return Result.success(products);
}
```

**亮点**：`wrapper.like(name != null, ...)` — 第一个参数是条件，为 `true` 时才拼这个 WHERE 条件。这样就不用写一堆 `if (name != null)` 了。

## 五、分页查询

### 5.1 配置分页插件

分页功能需要先注册一个插件：

```java
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}
```

### 5.2 使用分页

```java
@GetMapping("/page")
public Result<IPage<Product>> page(
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize
) {
    Page<Product> page = new Page<>(pageNum, pageSize);
    IPage<Product> result = productService.page(page);
    return Result.success(result);
}
```

返回的 `IPage` 包含：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "records": [...],    // 当前页数据
        "total": 6,          // 总记录数
        "size": 10,          // 每页条数
        "current": 1,        // 当前页码
        "pages": 1           // 总页数
    }
}
```

### 5.3 分页 + 条件组合

```java
@GetMapping("/page")
public Result<IPage<Product>> page(
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(required = false) String name
) {
    Page<Product> page = new Page<>(pageNum, pageSize);
    LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
    wrapper.like(name != null, Product::getName, name);

    IPage<Product> result = productService.page(page, wrapper);
    return Result.success(result);
}
```

## 六、逻辑删除

### 6.1 什么是逻辑删除

- **物理删除**：`DELETE FROM product WHERE id = 1` — 数据真的没了，不可恢复
- **逻辑删除**：`UPDATE product SET deleted = 1 WHERE id = 1` — 数据还在，只是标记为已删除

企业项目几乎都用逻辑删除，因为数据是最值钱的东西，不能真删。

### 6.2 MyBatis Plus 自动处理

在 Entity 上加了 `@TableLogic` 后，MyBatis Plus 会自动：

- 调用 `removeById(1)` → 实际执行 `UPDATE product SET deleted = 1 WHERE id = 1`
- 调用 `list()` → 实际执行 `SELECT * FROM product WHERE deleted = 0`

你不需要手动加 `WHERE deleted = 0`，框架全自动处理。

## 七、完整项目结构

```
mybatis-plus-demo/
├── pom.xml
└── src/main/java/com/study/mybatisplusdemo/
    ├── MybatisPlusDemoApplication.java       ← 启动类
    ├── config/
    │   └── MybatisPlusConfig.java            ← 分页插件配置
    ├── common/
    │   └── Result.java                       ← 统一响应（上节写过）
    ├── entity/
    │   └── Product.java                      ← 实体类
    ├── mapper/
    │   └── ProductMapper.java                ← Mapper 接口
    ├── service/
    │   ├── ProductService.java               ← Service 接口
    │   └── impl/
    │       └── ProductServiceImpl.java        ← Service 实现
    └── controller/
        └── ProductController.java            ← 控制层
```

## 练习

### 练习 1：搭建项目并实现商品 CRUD

在 `03-MyBatisPlus/` 目录下创建 `mybatis-plus-demo` 项目：

1. 先在 MySQL 中执行建表 SQL（第二节的 SQL）
2. 创建完整的项目结构（Entity → Mapper → Service → Controller）
3. 实现以下接口：
   - `GET /products/{id}` — 根据 ID 查询
   - `GET /products` — 查询全部
   - `POST /products` — 创建商品
   - `PUT /products/{id}` — 更新商品
   - `DELETE /products/{id}` — 删除商品（逻辑删除）

注意：`application.yml` 中的数据库密码要改成你自己的。

### 练习 2：条件查询 + 分页

在 Controller 中新增接口：

1. `GET /products/search?name=手机&minPrice=1000&maxPrice=8000` — 条件搜索（用 LambdaQueryWrapper）
2. `GET /products/page?pageNum=1&pageSize=3&name=手机` — 分页查询（记得先配置分页插件）

### 练习 3：思考题

1. `BaseMapper` 和 `IService` 有什么区别？为什么要有 Service 层？
2. `LambdaQueryWrapper` 比 `QueryWrapper` 好在哪里？
3. 逻辑删除和物理删除的区别？企业项目为什么优先用逻辑删除？
4. 金额字段为什么不能用 Double，要用 BigDecimal？
