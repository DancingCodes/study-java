# 注册中心 Nacos

## 一、为什么需要注册中心

### 1.1 没有注册中心的问题

订单服务要调用商品服务，得知道商品服务的地址：

```java
String url = "http://192.168.1.10:8081/product/1";
```

问题：
- 地址写死了，商品服务换机器就得改代码
- 商品服务部署了多台，怎么选择？
- 商品服务挂了一台，怎么知道？

### 1.2 有了注册中心

```
1. 商品服务启动 → 向 Nacos 注册自己的地址
2. 订单服务要调用 → 从 Nacos 获取商品服务的地址列表
3. 商品服务挂了 → Nacos 自动剔除
4. 商品服务扩容 → 新实例自动注册
```

类比：Nacos 就是一个**通讯录**，所有服务把自己的「电话号码」登记上去，需要打电话时查通讯录就行。

## 二、Nacos 简介

Nacos = **Na**ming + **Co**nfiguration **S**ervice

两大功能：
- **服务注册与发现**：服务上线自动注册，下线自动剔除
- **配置中心**：统一管理配置，支持动态刷新（不用重启应用）

## 三、Nacos 安装

### 3.1 下载

官网下载：https://github.com/alibaba/nacos/releases

选择最新稳定版（如 2.3.x），下载 `nacos-server-2.3.x.zip`。

### 3.2 启动

解压后，在 `bin` 目录下执行：

```bash
# Windows（单机模式）
startup.cmd -m standalone

# Linux/Mac
sh startup.sh -m standalone
```

### 3.3 验证

浏览器访问 http://localhost:8848/nacos

- 账号：`nacos`
- 密码：`nacos`

看到管理界面即启动成功。左侧菜单「服务管理 → 服务列表」目前是空的，等注册服务后就有了。

## 四、Spring Boot 整合 Nacos

### 4.1 父项目搭建

微服务项目通常用 Maven 多模块结构：

```
cloud-demo/
├── pom.xml              ← 父项目（管理依赖版本）
├── product-service/     ← 商品服务
├── order-service/       ← 订单服务
└── common/              ← 公共模块（实体类、工具类）
```

父项目 pom.xml 关键配置：

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.3</version>
</parent>

<properties>
    <spring-cloud.version>2023.0.0</spring-cloud.version>
    <spring-cloud-alibaba.version>2023.0.0.0-RC1</spring-cloud-alibaba.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>${spring-cloud-alibaba.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 4.2 子服务引入 Nacos

```xml
<!-- 服务发现 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

### 4.3 配置服务注册

```yaml
spring:
  application:
    name: product-service   # 服务名（注册到 Nacos 的名字）
  cloud:
    nacos:
      server-addr: localhost:8848   # Nacos 地址

server:
  port: 8081
```

启动类不需要额外注解，引入依赖后自动注册。

启动后在 Nacos 控制台的「服务列表」中可以看到 `product-service`。

### 4.4 同时注册多个服务

商品服务（8081）和订单服务（8082）分别配置不同的 `spring.application.name` 和端口，都注册到 Nacos：

```
Nacos 服务列表：
├── product-service  →  192.168.1.10:8081
└── order-service    →  192.168.1.10:8082
```

### 4.5 同一服务多实例

启动多个商品服务实例（不同端口），Nacos 自动识别：

```
product-service:
├── 192.168.1.10:8081
└── 192.168.1.10:8083
```

## 五、Nacos 配置中心（了解）

除了服务注册，Nacos 还能做配置中心：

```xml
<!-- 配置中心 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

```yaml
spring:
  cloud:
    nacos:
      config:
        server-addr: localhost:8848
        file-extension: yaml
```

在 Nacos 控制台「配置管理 → 配置列表」中新建配置，Data ID 为 `product-service.yaml`，内容就是 application.yml 的配置。

好处：修改配置不需要重启应用，Nacos 推送变更，应用自动刷新。

这个功能后续实战项目中再详细用，这里了解即可。

## 六、Nacos 核心概念

| 概念 | 说明 |
|------|------|
| **命名空间（Namespace）** | 隔离环境（dev/test/prod） |
| **分组（Group）** | 同一命名空间下再分组 |
| **服务名（Service Name）** | 就是 `spring.application.name` |
| **实例（Instance）** | 一个服务的一个运行节点（IP:Port） |
| **健康检查** | Nacos 定期检测实例是否存活 |

## 练习

### 练习 1：搭建多模块项目

1. 创建父项目 `cloud-demo`
2. 创建子模块 `product-service`（端口 8081）和 `order-service`（端口 8082）
3. 两个服务都注册到 Nacos
4. 在 Nacos 控制台查看服务列表

### 练习 2：多实例注册

启动两个 product-service 实例（8081 和 8083），在 Nacos 控制台查看实例列表。

### 练习 3：思考题

1. 注册中心解决了什么问题？
2. 如果 Nacos 挂了，已经在运行的服务还能互相调用吗？
3. Nacos 和 Eureka 有什么区别？（了解即可）
