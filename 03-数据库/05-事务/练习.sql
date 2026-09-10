-- ============================
-- 先确保使用 study_db 数据库
-- ============================
USE study_db;

-- ============================
-- 练习 1：事务基本操作 — 模拟转账
-- ============================
-- 1.1 创建 account 表，插入张三（余额 1000）和李四（余额 500）
-- TODO: 在这里写建表和插入语句
CREATE TABLE account
(
    id     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    name   VARCHAR(50) NOT NULL COMMENT '姓名',
    balance DECIMAL(10, 2) COMMENT '薪资'
);
INSERT INTO account (name, balance)
VALUES ('张三',1000.00),
       ('李四',500.00);


-- 1.2 开启事务，执行转账：张三 -200，李四 +200，然后提交
-- TODO: 在这里写事务语句

-- 开启事务
START TRANSACTION;
-- 执行操作
UPDATE account SET balance = balance - 200 WHERE name = '张三';
UPDATE account SET balance = balance + 200 WHERE name = '李四';
-- 全部成功 → 提交
COMMIT;

-- 1.3 查询两人余额，确认转账成功
-- TODO: 在这里写 SQL
SELECT * FROM account;

-- ============================
-- 练习 2：回滚测试
-- ============================
-- 2.1 开启事务
-- TODO: 在这里写 SQL
START TRANSACTION;
-- 2.2 把张三余额改为 0
-- TODO: 在这里写 SQL
UPDATE account SET balance = 0 WHERE name = '张三';
-- 2.3 执行回滚
-- TODO: 在这里写 SQL
ROLLBACK;
-- 2.4 查询张三余额，确认回滚成功（应该还是 800）
-- TODO: 在这里写 SQL
SELECT * FROM account WHERE name = '张三';

-- ============================
-- 练习 3：思考题
-- ============================
-- 3.1 如果不用事务，转账过程中数据库崩了，可能出现什么问题？
-- 回答：数据错乱

-- 3.2 MySQL 默认隔离级别是什么？它能防止哪些并发问题？
-- 回答：REPEATABLE READ,防止脏读和不可重复读

-- 3.3 脏读和不可重复读的区别是什么？
-- 回答：脏读是事务 A 读到了事务 B 还没提交的数据，不可重复读是事务 A 两次读同一行数据，中间事务 B 修改并提交了，导致 A 前后读到的值不一样。
