-- ============================
-- 先确保使用 study_db 数据库
-- ============================
USE study_db;

-- ============================
-- 准备工作：创建部门表（练习 3 需要用到）
-- ============================
CREATE TABLE department
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '部门ID',
    dept_name VARCHAR(50) NOT NULL COMMENT '部门名称',
    manager   VARCHAR(50) COMMENT '部门经理'
);

INSERT INTO department (dept_name, manager)
VALUES ('技术部', '王总'),
       ('产品部', '刘总'),
       ('销售部', '陈总'),
       ('人事部', '赵总');


-- ============================
-- 练习 1：聚合与分组
-- ============================
-- 1.1 查询员工总人数
-- TODO: 在这里写 SQL
SELECT COUNT(*)
FROM employee;

-- 1.2 查询技术部的平均薪资
-- TODO: 在这里写 SQL
SELECT AVG(salary) FROM employee WHERE department = '技术部';

-- 1.3 查询每个部门的人数和平均薪资
-- TODO: 在这里写 SQL
SELECT department, COUNT(*) AS '人数', AVG(salary) AS '平均薪资'
FROM employee
GROUP BY department;

-- 1.4 查询平均薪资大于 13000 的部门
-- TODO: 在这里写 SQL
SELECT department, AVG(salary) AS avg_salary
FROM employee
GROUP BY department
HAVING avg_salary > 13000;

-- ============================
-- 练习 2：子查询
-- ============================
-- 2.1 查询薪资最高的员工姓名和薪资
-- TODO: 在这里写 SQL
SELECT *
FROM employee
WHERE salary = (SELECT MAX(salary) FROM employee);

-- 2.2 查询薪资高于平均薪资的所有员工
-- TODO: 在这里写 SQL
SELECT *
FROM employee
WHERE salary > (SELECT AVG(salary) FROM employee);

-- 2.3 查询和王五同部门的所有员工
-- TODO: 在这里写 SQL
SELECT *
FROM employee
WHERE department = (SELECT department FROM employee WHERE name = '王五');

-- ============================
-- 练习 3：JOIN
-- ============================
-- 3.1 用 INNER JOIN 查询所有员工的姓名、薪资和部门经理
-- TODO: 在这里写 SQL
SELECT e.name, e.salary, d.manager
FROM employee e
         INNER JOIN department d ON e.department = d.dept_name;

-- 3.2 用 LEFT JOIN 查询每个部门的员工数（包含没有员工的部门）
-- TODO: 在这里写 SQL
SELECT d.dept_name, COUNT(e.id) AS '员工数'
FROM department d
         LEFT JOIN employee e ON d.dept_name = e.department
GROUP BY d.dept_name;

-- 3.3 查询没有员工的部门名称（提示：LEFT JOIN 后用 WHERE 筛选 NULL）
-- TODO: 在这里写 SQL
SELECT d.dept_name
FROM department d
         LEFT JOIN employee e ON d.dept_name = e.department
WHERE e.id IS NULL;
-- ============================
-- 练习 4：分页
-- ============================
-- 4.1 按薪资从高到低排序，查询第 2 页的数据（每页 3 条）
-- TODO: 在这里写 SQL
SELECT *
FROM employee
ORDER BY salary DESC
LIMIT 3, 3;