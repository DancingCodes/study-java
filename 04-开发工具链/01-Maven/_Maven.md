# Maven

## 一、什么是 Maven

Maven 是 Java 项目的**构建和依赖管理工具**。它帮你做三件事：

1. **依赖管理** — 自动下载和管理第三方 jar 包（不用手动下载放进项目）
2. **项目构建** — 编译、测试、打包、部署一条命令搞定
3. **项目结构标准化** — 所有 Maven 项目目录结构一致，换个项目也能立刻上手

类比：Maven 就像 JS 的 npm/yarn，`pom.xml` 就像 `package.json`。

## 二、Maven 项目结构

```
my-project/
├── pom.xml                    ← 项目配置文件（最核心）
├── src/
│   ├── main/
│   │   ├── java/              ← Java 源代码
│   │   │   └── com/example/
│   │   │       └── App.java
│   │   └── resources/         ← 配置文件（application.yml 等）
│   └── test/
│       ├── java/              ← 测试代码
│       └── resources/         ← 测试配置
└── target/                    ← 编译输出目录（自动生成，不用管）
```

**记住**：代码放 `src/main/java`，配置放 `src/main/resources`，测试放 `src/test`。

## 三、pom.xml — 项目核心配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!-- 项目坐标：唯一标识一个项目 -->
    <groupId>com.example</groupId>      <!-- 组织/公司域名倒写 -->
    <artifactId>my-project</artifactId> <!-- 项目名 -->
    <version>1.0.0</version>            <!-- 版本号 -->
    <packaging>jar</packaging>          <!-- 打包方式：jar/war/pom -->

    <!-- 依赖管理 -->
    <dependencies>
        <!-- 每个依赖一个 dependency -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.33</version>
        </dependency>
    </dependencies>
</project>
```

### 3.1 坐标（GAV）

每个 Maven 项目/依赖由三个值唯一确定：
- **G**roupId — 组织标识，如 `com.example`、`org.springframework`
- **A**rtifactId — 项目名，如 `my-project`、`spring-boot-starter`
- **V**ersion — 版本号，如 `1.0.0`、`3.2.5`

就像快递地址：省（groupId）+ 市（artifactId）+ 门牌号（version）。

### 3.2 添加依赖

需要用什么库，去 [Maven 中央仓库](https://mvnrepository.com/) 搜索，复制坐标粘贴到 `<dependencies>` 里。

```xml
<dependencies>
    <!-- MySQL 驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>

    <!-- Lombok（简化代码） -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.30</version>
        <scope>provided</scope>
    </dependency>

    <!-- JUnit 5（测试） -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### 3.3 依赖范围 scope

| scope | 编译 | 测试 | 运行 | 说明 |
|-------|------|------|------|------|
| compile（默认） | ✅ | ✅ | ✅ | 大部分依赖都用这个 |
| test | ❌ | ✅ | ❌ | 只在测试时用，如 JUnit |
| provided | ✅ | ✅ | ❌ | 编译时需要，运行时由容器提供，如 Lombok、Servlet |
| runtime | ❌ | ✅ | ✅ | 编译时不需要，运行时需要，如 MySQL 驱动 |

## 四、Maven 生命周期

Maven 构建项目分几个阶段，按顺序执行：

```
clean → compile → test → package → install → deploy
```

| 阶段 | 作用 | 对应命令 |
|------|------|----------|
| clean | 清除 target 目录 | `mvn clean` |
| compile | 编译 src/main/java | `mvn compile` |
| test | 执行测试 | `mvn test` |
| package | 打包（生成 jar/war） | `mvn package` |
| install | 安装到本地仓库 | `mvn install` |
| deploy | 部署到远程仓库 | `mvn deploy` |

**执行某个阶段会自动执行它之前的所有阶段**。比如 `mvn package` 会先 compile → test → package。

最常用的组合：
```bash
mvn clean package          # 清理 + 打包
mvn clean package -DskipTests  # 清理 + 打包，跳过测试
mvn clean install          # 清理 + 安装到本地仓库
```

## 五、Maven 仓库

```
依赖查找顺序：
本地仓库 → 远程仓库（私服） → 中央仓库
```

- **本地仓库**：你电脑上的缓存，默认在 `~/.m2/repository`
- **中央仓库**：Maven 官方仓库，所有开源 jar 都在这
- **私服**：公司内部搭建的仓库（后面工作中会接触）

### 配置阿里云镜像（加速下载）

中央仓库在国外，下载很慢。在 `~/.m2/settings.xml` 配置阿里云镜像：

```xml
<mirrors>
    <mirror>
        <id>aliyun</id>
        <name>Aliyun Maven Mirror</name>
        <url>https://maven.aliyun.com/repository/public</url>
        <mirrorOf>central</mirrorOf>
    </mirror>
</mirrors>
```

## 六、IDEA 中使用 Maven

### 6.1 创建 Maven 项目

File → New → Project → 选择 Maven → 填写 GroupId 和 ArtifactId → 完成

### 6.2 IDEA 右侧 Maven 面板

点击右侧 **Maven** 面板可以看到：
- **Lifecycle** — 生命周期按钮（双击执行）
- **Dependencies** — 依赖树（看有哪些依赖）

### 6.3 常用操作

- 修改 pom.xml 后 → 点击右上角 **刷新图标**（或 Ctrl+Shift+O）让 IDEA 重新加载依赖
- 依赖下载失败 → 右键 Maven 面板 → **Reload All Maven Projects**

## 七、多模块项目（了解）

大项目会拆成多个模块，每个模块是一个子项目：

```
my-app/
├── pom.xml                 ← 父 pom（packaging 为 pom）
├── my-app-common/          ← 公共模块
│   └── pom.xml
├── my-app-service/         ← 业务模块
│   └── pom.xml
└── my-app-web/             ← Web 模块
    └── pom.xml
```

父 pom 统一管理版本和公共依赖，子模块继承父 pom。后面学 Spring Boot 实战时会用到。

## 练习

### 练习 1：创建 Maven 项目

在 IDEA 中创建一个 Maven 项目：
- GroupId：`com.study`
- ArtifactId：`maven-demo`
- Version：`1.0.0`

创建完成后，确认目录结构是否符合标准 Maven 结构。

### 练习 2：添加依赖

在 pom.xml 中添加以下依赖：
1. MySQL 驱动（mysql-connector-java 8.0.33）
2. Lombok（1.18.30，scope 为 provided）
3. JUnit 5（junit-jupiter 5.10.1，scope 为 test）

添加后刷新 Maven，确认依赖下载成功。

### 练习 3：执行构建命令

在 IDEA 的 Terminal 或 Maven 面板中执行：
1. `mvn clean` — 清理 target 目录
2. `mvn compile` — 编译项目
3. `mvn package` — 打包，看 target 目录下有没有生成 jar 文件

### 练习 4：思考题（口头回答即可）

1. pom.xml 相当于 JS 中的什么文件？
2. `mvn package` 执行时，会自动先执行哪些阶段？
3. 如果只想在测试时用某个依赖，scope 应该设置为什么？
