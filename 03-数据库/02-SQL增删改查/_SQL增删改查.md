# SQL 增删改查

## 一、INSERT — 插入数据

### 1.1 插入单条数据

```sql
-- 指定所有字段
INSERT INTO employee (name, department, salary, hire_date)
VALUES ('张三', '技术部', 15000.00, '2024-03-01');

-- id 和 create_time 不用写，id 自增，create_time 有默认值
```

### 1.2 插入多条数据

```sql
INSERT INTO employee (name, department, salary, hire_date) VALUES
('李四', '产品部', 12000.00, '2024-05-15'),
('王五', '技术部', 18000.00, '2023-08-20'),
('赵六', '销售部', 10000.00, '2024-01-10'),
('钱七', '技术部', 20000.00, '2023-01-05');
```

## 二、SELECT — 查询数据

查询是 SQL 中**最常用**的操作。

### 2.1 基本查询

```sql
-- 查询所有字段
SELECT * FROM employee;

-- 查询指定字段
SELECT name, salary FROM employee;

-- 给字段起别名（AS 可省略）
SELECT name AS '姓名', salary AS '薪资' FROM employee;
```

### 2.2 条件查询 WHERE

```sql
-- 等于
SELECT * FROM employee WHERE department = '技术部';

-- 不等于
SELECT * FROM employee WHERE department != '技术部';

-- 大于、小于、大于等于、小于等于
SELECT * FROM employee WHERE salary > 15000;
SELECT * FROM employee WHERE salary >= 15000;

-- BETWEEN...AND（范围，包含两端）
SELECT * FROM employee WHERE salary BETWEEN 10000 AND 15000;

-- IN（多个值中的一个）
SELECT * FROM employee WHERE department IN ('技术部', '产品部');

-- LIKE（模糊查询）
SELECT * FROM employee WHERE name LIKE '张%';   -- 以"张"开头
SELECT * FROM employee WHERE name LIKE '%三';   -- 以"三"结尾
SELECT * FROM employee WHERE name LIKE '%五%';  -- 包含"五"

-- IS NULL / IS NOT NULL
SELECT * FROM employee WHERE department IS NULL;
SELECT * FROM employee WHERE department IS NOT NULL;
```

### 2.3 多条件组合

```sql
-- AND（同时满足）
SELECT * FROM employee WHERE department = '技术部' AND salary > 15000;

-- OR（满足其一）
SELECT * FROM employee WHERE department = '技术部' OR department = '产品部';

-- NOT（取反）
SELECT * FROM employee WHERE NOT department = '销售部';
```

### 2.4 排序 ORDER BY

```sql
-- 升序（默认）
SELECT * FROM employee ORDER BY salary;
SELECT * FROM employee ORDER BY salary ASC;

-- 降序
SELECT * FROM employee ORDER BY salary DESC;

-- 多字段排序（先按部门升序，部门相同按薪资降序）
SELECT * FROM employee ORDER BY department ASC, salary DESC;
```

### 2.5 去重 DISTINCT

```sql
-- 查看有哪些部门（去重）
SELECT DISTINCT department FROM employee;
```

### 2.6 限制条数 LIMIT

```sql
-- 取前 3 条
SELECT * FROM employee LIMIT 3;

-- 从第 2 条开始取 3 条（偏移量从 0 开始）
SELECT * FROM employee LIMIT 1, 3;
```

## 三、UPDATE — 修改数据

```sql
-- 修改指定条件的数据
UPDATE employee SET salary = 16000 WHERE name = '张三';

-- 同时修改多个字段
UPDATE employee SET salary = 13000, department = '技术部' WHERE name = '李四';

-- ⚠️ 不加 WHERE 会修改所有行！
UPDATE employee SET salary = 0;  -- 危险！全员薪资变 0
```

## 四、DELETE — 删除数据

```sql
-- 删除指定条件的数据
DELETE FROM employee WHERE name = '赵六';

-- ⚠️ 不加 WHERE 会删除所有数据！
DELETE FROM employee;  -- 危险！清空整张表

-- 清空表（比 DELETE 快，直接重置表）
TRUNCATE TABLE employee;
```

## 五、SQL 执行顺序

写的时候是 `SELECT ... FROM ... WHERE ... ORDER BY ... LIMIT`，但实际执行顺序是：

1. `FROM` — 先确定从哪张表查
2. `WHERE` — 筛选条件
3. `SELECT` — 选择要显示的字段
4. `ORDER BY` — 排序
5. `LIMIT` — 限制条数

记住这个顺序，后面学复杂查询会用到。

## 练习

### 练习 1：插入数据

往 `employee` 表中插入以下 5 条数据：

| name | department | salary | hire_date |
|------|-----------|--------|-----------|
| 孙八 | 销售部 | 9000.00 | 2024-06-01 |
| 周九 | 技术部 | 22000.00 | 2022-11-15 |
| 吴十 | 产品部 | 14000.00 | 2024-02-20 |
| 郑十一 | 技术部 | 17000.00 | 2023-07-08 |
| 陈十二 | 销售部 | 11000.00 | 2024-04-25 |

### 练习 2：查询练习

用 SELECT 语句完成以下查询（每个写一条 SQL）：

1. 查询所有技术部的员工姓名和薪资
2. 查询薪资大于 15000 的员工所有信息
3. 查询薪资在 10000 到 18000 之间的员工
4. 查询姓"张"或姓"李"的员工
5. 查询所有员工，按薪资从高到低排序
6. 查询薪资最高的前 3 名员工

### 练习 3：修改和删除

1. 把孙八的部门改为"技术部"，薪资改为 12000
2. 给所有销售部的员工薪资加 1000（提示：`SET salary = salary + 1000`）
3. 删除薪资低于 10000 的员工
