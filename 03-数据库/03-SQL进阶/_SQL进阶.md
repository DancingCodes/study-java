# SQL 进阶

## 一、聚合函数

聚合函数对一组数据进行计算，返回一个结果。

```sql
-- COUNT：计数
SELECT COUNT(*) FROM employee;              -- 总共多少条数据
SELECT COUNT(*) FROM employee WHERE department = '技术部';  -- 技术部多少人

-- SUM：求和
SELECT SUM(salary) FROM employee;           -- 所有人薪资总和

-- AVG：平均值
SELECT AVG(salary) FROM employee;           -- 平均薪资

-- MAX / MIN：最大值 / 最小值
SELECT MAX(salary) FROM employee;           -- 最高薪资
SELECT MIN(salary) FROM employee;           -- 最低薪资
```

## 二、分组 GROUP BY

按某个字段分组，配合聚合函数使用。

```sql
-- 每个部门有多少人
SELECT department, COUNT(*) AS '人数' FROM employee GROUP BY department;

-- 每个部门的平均薪资
SELECT department, AVG(salary) AS '平均薪资' FROM employee GROUP BY department;

-- 每个部门的最高薪资和最低薪资
SELECT department, MAX(salary) AS '最高', MIN(salary) AS '最低'
FROM employee GROUP BY department;
```

### HAVING — 对分组结果再筛选

`WHERE` 是在分组前筛选，`HAVING` 是在分组后筛选。

```sql
-- 查询平均薪资大于 15000 的部门
SELECT department, AVG(salary) AS avg_salary
FROM employee
GROUP BY department
HAVING avg_salary > 15000;

-- ❌ 错误写法：不能用 WHERE 筛选聚合结果
SELECT department, AVG(salary) FROM employee WHERE AVG(salary) > 15000 GROUP BY department;
```

**记忆**：WHERE 筛选行，HAVING 筛选组。

## 三、子查询

把一个 SELECT 的结果当作另一个查询的条件或数据源，就是子查询。

### 3.1 作为条件（WHERE 中）

```sql
-- 查询薪资最高的员工（不知道最高是多少，用子查询算）
SELECT * FROM employee WHERE salary = (SELECT MAX(salary) FROM employee);

-- 查询薪资高于平均薪资的员工
SELECT * FROM employee WHERE salary > (SELECT AVG(salary) FROM employee);

-- 查询和张三同部门的员工
SELECT * FROM employee
WHERE department = (SELECT department FROM employee WHERE name = '张三');
```

### 3.2 作为临时表（FROM 中）

```sql
-- 查询每个部门薪资最高的那个人
-- 先分组算出每个部门的最高薪资，再用这个结果去匹配
SELECT e.*
FROM employee e
INNER JOIN (
    SELECT department, MAX(salary) AS max_salary
    FROM employee
    GROUP BY department
) t ON e.department = t.department AND e.salary = t.max_salary;
```

### 3.3 IN 子查询

```sql
-- 查询技术部和产品部的员工（用子查询方式）
SELECT * FROM employee
WHERE department IN (SELECT DISTINCT department FROM employee WHERE department IN ('技术部', '产品部'));
```

## 四、JOIN — 多表联查

实际项目中数据分布在多张表里，需要 JOIN 把它们关联起来查询。

先建一张部门表来演示：

```sql
CREATE TABLE department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '部门ID',
    dept_name VARCHAR(50) NOT NULL COMMENT '部门名称',
    manager VARCHAR(50) COMMENT '部门经理'
);

INSERT INTO department (dept_name, manager) VALUES
('技术部', '王总'),
('产品部', '刘总'),
('销售部', '陈总'),
('人事部', '赵总');
```

### 4.1 INNER JOIN（内连接）

只返回两张表都能匹配上的数据。

```sql
-- 查询员工及其部门经理
SELECT e.name, e.department, d.manager
FROM employee e
INNER JOIN department d ON e.department = d.dept_name;
```

`e` 和 `d` 是表的别名，用 `ON` 指定关联条件。

### 4.2 LEFT JOIN（左连接）

返回左表所有数据，右表匹配不上的显示 NULL。

```sql
-- 查询所有部门及其员工数（包括没有员工的部门）
SELECT d.dept_name, COUNT(e.id) AS '员工数'
FROM department d
LEFT JOIN employee e ON d.dept_name = e.department
GROUP BY d.dept_name;
```

人事部没有员工，但也会显示出来，员工数为 0。

### 4.3 RIGHT JOIN（右连接）

和 LEFT JOIN 相反，返回右表所有数据。实际中**用得少**，一般用 LEFT JOIN 调换表顺序即可。

### 4.4 各种 JOIN 的区别

```
     A          B
  [1,2,3]   [2,3,4]

INNER JOIN → [2,3]      两边都有的
LEFT JOIN  → [1,2,3]    左边全保留，右边匹配不上为 NULL
RIGHT JOIN → [2,3,4]    右边全保留，左边匹配不上为 NULL
```

## 五、分页查询

实际项目中数据量大，需要分页显示。MySQL 用 `LIMIT offset, size`。

```sql
-- 第 1 页（每页 3 条）
SELECT * FROM employee ORDER BY id LIMIT 0, 3;

-- 第 2 页
SELECT * FROM employee ORDER BY id LIMIT 3, 3;

-- 第 3 页
SELECT * FROM employee ORDER BY id LIMIT 6, 3;

-- 公式：LIMIT (页码 - 1) * 每页条数, 每页条数
```

## 六、完整的 SELECT 执行顺序

```sql
SELECT     -- 5. 选择字段
FROM       -- 1. 确定表
JOIN ON    -- 2. 关联表
WHERE      -- 3. 筛选行
GROUP BY   -- 4. 分组
HAVING     -- 6. 筛选组
ORDER BY   -- 7. 排序
LIMIT      -- 8. 分页
```

## 练习

### 练习 1：聚合与分组

1. 查询员工总人数
2. 查询技术部的平均薪资
3. 查询每个部门的人数和平均薪资
4. 查询平均薪资大于 13000 的部门

### 练习 2：子查询

1. 查询薪资最高的员工姓名和薪资
2. 查询薪资高于平均薪资的所有员工
3. 查询和王五同部门的所有员工

### 练习 3：JOIN

先执行上面的建表语句创建 `department` 表并插入数据，然后：

1. 用 INNER JOIN 查询所有员工的姓名、薪资和部门经理
2. 用 LEFT JOIN 查询每个部门的员工数（包含没有员工的部门）
3. 查询没有员工的部门名称（提示：LEFT JOIN 后用 WHERE 筛选 NULL）

### 练习 4：分页

1. 按薪资从高到低排序，查询第 2 页的数据（每页 3 条）
