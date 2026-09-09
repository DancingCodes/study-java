# Java 概述

## 一、Java 的历史

- **1991 年**：Sun 公司的 James Gosling 团队开始开发一种叫 "Oak"（橡树）的语言，最初是给电视机顶盒等嵌入式设备用的
- **1995 年**：正式更名为 **Java**，发布 Java 1.0。口号是 **"Write Once, Run Anywhere"**（一次编写，到处运行）
- **2006 年**：Sun 公司将 Java 开源
- **2009 年**：Oracle（甲骨文）收购 Sun 公司，Java 归 Oracle 所有
- **2014 年**：发布 **Java 8**（里程碑版本，引入 Lambda、Stream API，至今仍被大量使用）
- **2017 年**：Java 改为每 **6 个月**发布一个新版本（9, 10, 11, 12...）
- **2018 年**：发布 **Java 11**（第一个长期支持版本 LTS）
- **2021 年**：发布 **Java 17**（LTS，Spring Boot 3 最低要求）
- **2023 年**：发布 **Java 21**（最新 LTS）

### 什么是 LTS？

LTS = Long-Term Support（长期支持版本）。Oracle 会为 LTS 版本提供多年的安全更新。

**企业项目选版本就认 LTS**：Java 8、11、17、21。其他版本（9、10、12...）是过渡版，半年就不维护了。

## 二、Java 现在的地位

- 连续 20 多年位居编程语言排行榜前三（TIOBE）
- 全球约有 **900 万+** Java 开发者
- 主要应用领域：

| 领域 | 说明 |
|------|------|
| **企业后端** | 银行、电商、政务系统的核心服务（Java 最大的战场） |
| **Android 开发** | Android 应用的主要开发语言（现在 Kotlin 也很多） |
| **大数据** | Hadoop、Spark、Flink、Kafka 都是 Java/Scala 写的 |
| **微服务** | Spring Cloud、Dubbo 等微服务框架生态成熟 |

简单说：**后端开发、企业级应用，Java 是绝对的主力**。

## 三、Java 为什么能跨平台？

其他语言（如 C）编译后直接变成机器码，只能在特定操作系统上运行。

Java 不一样：

```
你写的代码（.java）
      │
      ▼  javac 编译
字节码（.class）      ← 不是机器码，是中间产物
      │
      ▼  JVM 解释执行
机器码              ← 不同操作系统有不同的 JVM
```

- **JVM**（Java Virtual Machine）= Java 虚拟机，负责把字节码翻译成当前操作系统能懂的机器码
- Windows 装 Windows 版的 JVM，Mac 装 Mac 版的 JVM，Linux 装 Linux 版的
- 你的 `.class` 文件不用改，放到哪个系统都能跑 → **一次编写，到处运行**

## 四、JDK、JRE、JVM 的关系

```
JDK（Java Development Kit）开发工具包
 ├── JRE（Java Runtime Environment）运行环境
 │    ├── JVM（Java Virtual Machine）虚拟机
 │    └── 核心类库（String、List、Math 等）
 └── 开发工具（javac 编译器、java 命令、jdb 调试器等）
```

- **JVM**：执行字节码的虚拟机
- **JRE**：JVM + 核心类库，只能**运行** Java 程序
- **JDK**：JRE + 开发工具，能**开发 + 运行** Java 程序

**你装 JDK 就够了**，JRE 和 JVM 都包含在里面。

## 五、JDK 安装与环境配置

### 1. 下载 JDK

推荐用 **Amazon Corretto**（免费、稳定、企业用得多）或 **Oracle JDK**：

- Amazon Corretto 17：https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html
- Oracle JDK 17：https://www.oracle.com/java/technologies/downloads/

下载 Windows x64 的 `.msi` 或 `.exe` 安装包，一路下一步装好。

### 2. 配置环境变量

安装完后需要告诉系统 JDK 在哪：

1. 右键「此电脑」→ 属性 → 高级系统设置 → 环境变量
2. 新建系统变量：
   - 变量名：`JAVA_HOME`
   - 变量值：JDK 安装路径（如 `C:\Program Files\Java\jdk-17`）
3. 编辑 `Path` 变量，新增一行：`%JAVA_HOME%\bin`

### 3. 验证安装

打开命令行（cmd），输入：

```bash
java -version
javac -version
```

看到版本号就说明装好了。

## 六、第一个程序：Hello World

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

逐行解释：

| 代码 | 含义 |
|------|------|
| `public` | 访问修饰符，表示这个类是公开的。**可以不写，但如果写了，类名必须和文件名一致**。实际开发中几乎都写 public |
| `class HelloWorld` | 定义一个类。**类名必须和文件名一致**（文件叫 HelloWorld.java，类就得叫 HelloWorld） |
| `static` | 静态方法，不需要创建对象就能调用。**main 方法必须是 static**，因为程序启动时还没有任何对象 |
| `void` | 返回值类型，void 表示这个方法不返回任何值 |
| `main` | 方法名。**JVM 规定程序入口就叫 main**，名字不能改 |
| `String[] args` | 参数，接收命令行传入的参数（现在用不到，但必须写） |
| `System.out.println(...)` | 打印一行文字到控制台，println 会自动换行 |

> **总结**：`public static void main(String[] args)` 这一整行是固定写法，一个字都不能少，JVM 只认这个签名作为程序入口。

### 编译和运行

```bash
# 编译：.java → .class
javac HelloWorld.java

# 运行：只写类名，不带任何后缀（不能写 HelloWorld.class）
java HelloWorld
```

> **注意**：`java HelloWorld.class` 会报错！`java` 命令后面跟的是**类名**，不是文件名。
>
> Java 11+ 也支持直接运行 `java HelloWorld.java`（自动编译+执行），但建议先掌握两步流程。

---

## 练习

### 练习 1：自我介绍
写一个程序，打印出以下三行（内容换成你自己的）：
```
我叫张三
我正在学 Java
我的目标是独立开发项目
```

### 练习 2：理解编译过程
1. 用 `javac` 编译你的 `Main.java`
2. 查看生成的 `.class` 文件
3. 用 `java` 命令运行它
4. 思考：如果把类名改成 `Hello`，但文件名还是 `Main.java`，编译会怎样？试一试。
