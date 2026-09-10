# MySQL 基础

## 一、什么是数据库

之前学的 Java 程序，数据都存在变量、集合里，程序一关数据就没了。数据库就是**专门用来持久化存储数据的软件**，程序关了数据还在。

常见数据库：
- **关系型数据库**（表格结构，用 SQL 操作）：MySQL、PostgreSQL、Oracle、SQL Server
- **非关系型数据库**（键值/文档等结构）：Redis、MongoDB

我们学 **MySQL**，它是最流行的开源关系型数据库，企业用得最多。

## 二、核心概念

把数据库想象成 Excel：

| 数据库概念 | Excel 对应 |
|-----------|------------|
| 数据库（Database） | 一个 Excel 文件 |
| 表（Table） | 文件里的一个 Sheet |
| 行（Row） | Sheet 里的一行数据 |
| 列（Column） | Sheet 里的一列（字段） |

比如一个 `student` 表：

| id | name | age | score |
|----|------|-----|-------|
| 1  | 张三  | 20  | 88    |
| 2  | 李四  | 21  | 95    |

## 三、安装 MySQL

Windows 上推荐用 **MySQL 8.0**，两种安装方式：

### 方式一：MySQL Installer（推荐新手）

1. 去 https://dev.mysql.com/downloads/installer/ 下载 MySQL Installer
2. 选择 "Developer Default" 安装
3. 安装过程中设置 root 密码（**一定要记住**）
4. 完成后在命令行输入 `mysql -u root -p`，能进入就说明安装成功

### 方式二：用 Docker（后面会学）

```bash
docker run -d --name mysql -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 mysql:8.0
```

## 四、SQL 语言简介

SQL（Structured Query Language）是操作数据库的语言，分为几类：

| 类型 | 全称 | 作用 | 常用语句 |
|------|------|------|----------|
| DDL | Data Definition Language | 定义数据库和表的结构 | CREATE、ALTER、DROP |
| DML | Data Manipulation Language | 操作表中的数据 | INSERT、UPDATE、DELETE |
| DQL | Data Query Language | 查询数据 | SELECT |
| DCL | Data Control Language | 权限控制 | GRANT、REVOKE |

SQL 语句**不区分大小写**，但约定关键字大写、表名列名小写。语句以 `;` 结尾。

## 五、建库建表

### 5.1 数据库操作

```sql
-- 查看所有数据库
SHOW DATABASES;

-- 创建数据库
CREATE DATABASE study_db;

-- 使用数据库（切换到这个库）
USE study_db;

-- 删除数据库（慎用！）
DROP DATABASE study_db;
```

### 5.2 数据类型

常用数据类型：

| 类型 | 说明 | 示例 |
|------|------|------|
| INT | 整数 | 年龄、数量 |
| BIGINT | 大整数 | ID（数据量大时） |
| DOUBLE | 小数 | 价格、分数 |
| DECIMAL(M,D) | 精确小数（M 位总长，D 位小数） | 金额 DECIMAL(10,2) |
| VARCHAR(N) | 可变长字符串（最多 N 个字符） | 姓名 VARCHAR(50) |
| TEXT | 长文本 | 文章内容 |
| DATE | 日期 | 2024-01-15 |
| DATETIME | 日期+时间 | 2024-01-15 10:30:00 |
| TIMESTAMP | 时间戳（自动记录修改时间） | 创建时间、更新时间 |

**选型建议**：
- 存钱用 `DECIMAL`，不用 `DOUBLE`（精度问题，和 Java 的 float/double 一样）
- 字符串优先用 `VARCHAR`，知道长度就限定
- ID 用 `BIGINT`，不要用 `INT`（数据量大了会不够）

### 5.3 建表

```sql
CREATE TABLE student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    age INT COMMENT '年龄',
    score DECIMAL(5,2) COMMENT '分数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);
```

关键字解释：
- `PRIMARY KEY` — 主键，唯一标识每一行，不能重复
- `AUTO_INCREMENT` — 自增，插入数据时 id 自动 +1
- `NOT NULL` — 不允许为空
- `DEFAULT` — 默认值
- `COMMENT` — 注释，说明这个字段是干什么的

### 5.4 查看和删除表

```sql
-- 查看当前库有哪些表
SHOW TABLES;

-- 查看表结构
DESC student;

-- 删除表（慎用！）
DROP TABLE student;
```

## 练习

### 练习 1：建库建表

安装好 MySQL 后，完成以下操作：

1. 创建一个数据库 `study_db`
2. 在 `study_db` 中创建一张 `employee` 表，包含以下字段：
   - `id`：主键，自增，BIGINT
   - `name`：姓名，不能为空，VARCHAR(50)
   - `department`：部门，VARCHAR(50)
   - `salary`：薪资，DECIMAL(10,2)
   - `hire_date`：入职日期，DATE
   - `create_time`：创建时间，DATETIME，默认当前时间
3. 用 `DESC employee;` 查看表结构，截图或把结果贴给我
