-- ============================
-- 练习 1：建库建表
-- ============================

-- 创建数据库
-- TODO: 在这里写 SQL
CREATE DATABASE study_db;

-- 使用数据库
-- TODO: 在这里写 SQL
USE study_db;

-- 创建员工表
-- TODO: 在这里写 SQL
CREATE TABLE employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    department VARCHAR(50) COMMENT '部门',
    salary DECIMAL(10,2) COMMENT '薪资',
    hire_date DATE COMMENT '入职日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- 查看表结构
-- TODO: 在这里写 SQL
DESC employee;
