# Maven 速查手册

## 标准目录结构（必须遵守）

```
my-project/
├── pom.xml                    ← 项目配置文件
├── src/
│   ├── main/
│   │   ├── java/              ← 源代码
│   │   └── resources/         ← 配置文件（如 application.yml）
│   └── test/
│       └── java/              ← 测试代码
└── target/                    ← 编译输出（自动生成，不提交到 Git）
```

- 这是 Maven 官方约定，叫 Standard Directory Layout
- 所有 Maven 项目都用这个结构，不要改
- IDE、插件、Spring Boot 都依赖这个结构

## 基础命令

| 命令 | 作用 |
|------|------|
| `mvn compile` | 编译 src/main/java 下的源代码 |
| `mvn test` | 编译 + 运行测试 |
| `mvn package` | 编译 + 测试 + 打成 jar 包（输出到 target/） |
| `mvn clean` | 删除 target/ 目录，清理上次的编译结果 |
| `mvn install` | 编译 + 测试 + 打包 + 安装到本地仓库（~/.m2/repository/） |

## 组合命令

| 命令 | 作用 |
|------|------|
| `mvn clean compile` | 先清理再编译（重新来过） |
| `mvn clean package` | 先清理再打包（最常用） |
| `mvn clean install` | 先清理再安装到本地仓库 |
| `mvn compile exec:java` | 编译并运行主类（需要 exec 插件） |

## 查看信息

| 命令 | 作用 |
|------|------|
| `mvn -version` | 查看 Maven 版本 |
| `mvn dependency:tree` | 查看项目的依赖树（谁依赖了谁） |
| `mvn help:effective-pom` | 查看最终生效的完整 pom.xml |

## 跳过测试

| 命令 | 作用 |
|------|------|
| `mvn package -DskipTests` | 打包但跳过测试执行 |
| `mvn package -Dmaven.test.skip=true` | 打包并跳过测试编译和执行 |

## 小贴士

- 命令的执行有顺序：compile → test → package → install，执行后面的会自动执行前面的
- 加 `clean` 是好习惯，避免旧的编译结果干扰
- `target/` 目录是自动生成的，不要提交到 Git
