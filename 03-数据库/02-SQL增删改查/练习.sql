-- ============================
-- 先确保使用 study_db 数据库
-- ============================
USE study_db;

-- ============================
-- 练习 1：插入数据
-- ============================
-- 往 employee 表中插入以下 5 条数据：
-- 孙八, 销售部, 9000.00, 2024-06-01
-- 周九, 技术部, 22000.00, 2022-11-15
-- 吴十, 产品部, 14000.00, 2024-02-20
-- 郑十一, 技术部, 17000.00, 2023-07-08
-- 陈十二, 销售部, 11000.00, 2024-04-25

-- TODO: 在这里写 INSERT 语句
INSERT INTO employee (name, department, salary, hire_date)
VALUES ('孙八', '销售部', 9000.00, '2024-06-01'),
       ('周九', '技术部', 22000.00, '2022-11-15'),
       ('吴十', '产品部', 14000.00, '2024-02-20'),
       ('郑十一', '技术部', 17000.00, '2023-07-08'),
       ('陈十二', '销售部', 11000.00, '2024-04-25');


-- ============================
-- 练习 2：查询练习
-- ============================
-- 2.1 查询所有技术部的员工姓名和薪资
-- TODO: 在这里写 SQL
SELECT name, salary FROM employee WHERE department = '技术部';

-- 2.2 查询薪资大于 15000 的员工所有信息
-- TODO: 在这里写 SQL
SELECT * FROM employee WHERE salary > 15000;

-- 2.3 查询薪资在 10000 到 18000 之间的员工
-- TODO: 在这里写 SQL
SELECT * FROM employee WHERE salary BETWEEN 10000 AND 18000;

-- 2.4 查询姓"张"或姓"李"的员工
-- TODO: 在这里写 SQL
SELECT * FROM employee WHERE name LIKE '张%' OR name LIKE '李%';

-- 2.5 查询所有员工，按薪资从高到低排序
-- TODO: 在这里写 SQL
SELECT * FROM employee ORDER BY salary DESC;

-- 2.6 查询薪资最高的前 3 名员工
-- TODO: 在这里写 SQL
SELECT * FROM employee ORDER BY salary DESC LIMIT 3;

-- ============================
-- 练习 3：修改和删除
-- ============================
-- 3.1 把孙八的部门改为"技术部"，薪资改为 12000
-- TODO: 在这里写 SQL
UPDATE employee SET salary = 12000, department = '技术部' WHERE name = '孙八';

-- 3.2 给所有销售部的员工薪资加 1000
-- TODO: 在这里写 SQL
UPDATE employee SET salary = salary +1000 WHERE department = '销售部';

-- 3.3 删除薪资低于 10000 的员工
-- TODO: 在这里写 SQL
DELETE FROM employee WHERE salary < 10000;