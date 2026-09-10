-- ============================
-- 先确保使用 study_db 数据库
-- ============================
USE study_db;

-- ============================
-- 练习 1：索引操作
-- ============================
-- 1.1 给 employee 表的 name 字段添加普通索引 idx_name
-- TODO: 在这里写 SQL
CREATE INDEX idx_name ON employee(name);

-- 1.2 给 employee 表的 department 和 salary 添加复合索引 idx_dept_salary
-- TODO: 在这里写 SQL
CREATE INDEX idx_dept_salary ON employee(department, salary);

-- 1.3 查看 employee 表的所有索引
-- TODO: 在这里写 SQL
SHOW INDEX FROM employee;

-- ============================
-- 练习 2：EXPLAIN 分析
-- ============================
-- 2.1 分析按主键查询
EXPLAIN SELECT * FROM employee WHERE id = 1;
-- type: const    key: PRIMARY

-- 2.2 分析按 name 查询
EXPLAIN SELECT * FROM employee WHERE name = '张三';
-- type: ref   key: idx_name

-- 2.3 分析按 department + salary 查询
EXPLAIN SELECT * FROM employee WHERE department = '技术部' AND salary > 15000;
-- type: range    key: idx_dept_salary

-- 2.4 分析只按 salary 查询
EXPLAIN SELECT * FROM employee WHERE salary > 15000;
-- type: ALL    key: null


-- ============================
-- 练习 3：思考题
-- ============================
-- 3.1 为什么第 2.4 题（只按 salary 查询）可能用不到复合索引 idx_dept_salary？
-- 回答：最左前缀原则

-- 3.2 WHERE name LIKE '%三' 能不能用到 idx_name 索引？为什么？
-- 回答：左模糊
