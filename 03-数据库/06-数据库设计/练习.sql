-- ============================
-- 先确保使用 study_db 数据库
-- ============================
USE study_db;

-- ============================
-- 练习 1：判断范式
-- ============================
-- 表 A：
-- | id | name | skills            |
-- |----|------|-------------------|
-- | 1  | 张三 | Java, Python, Go  |
-- 违反第几范式？怎么修正？
-- 回答：违反第一范式，拆分单独skills表

-- 表 B：
-- | student_id | course_id | student_name | course_name | score |
-- （联合主键：student_id + course_id）
-- 违反第几范式？怎么修正？
-- 回答：违反第二范式，拆成两张表

-- 表 C：
-- | order_id | product_id | product_name | product_price | quantity |
-- （主键：order_id）
-- 违反第几范式？怎么修正？
-- 回答：违反第三范式，拆表


-- ============================
-- 练习 2：设计博客系统表结构
-- ============================
-- 需求：
-- - 用户可以注册登录（用户名、密码、邮箱、头像）
-- - 用户可以发表文章（标题、内容、发布时间）
-- - 用户可以对文章评论（评论内容、评论时间）
-- - 文章有分类（如技术、生活、随笔），一篇文章只属于一个分类
--
-- 表关系分析：
-- ┌──────────┐       1:N      ┌─────────────┐       1:N      ┌─────────────┐
-- │  user    │───────────────→│  article     │───────────────→│  comment     │
-- └──────────┘                └─────────────┘                └─────────────┘
--                                   ↑ N:1
--                             ┌─────────────┐
--                             │  category    │
--                             └─────────────┘
--
-- user → article：1:N（一个用户发多篇文章）
-- user → comment：1:N（一个用户发多条评论）
-- article → comment：1:N（一篇文章有多条评论）
-- category → article：1:N（一个分类下有多篇文章）

-- 用户表
CREATE TABLE user
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    username    VARCHAR(50)  NOT NULL COMMENT '用户名',
    password    VARCHAR(50)  NOT NULL COMMENT '密码',
    email       VARCHAR(100) NOT NULL COMMENT '邮箱',
    avatar      VARCHAR(255) COMMENT '头像',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted  TINYINT  DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除'
);

-- 分类表
CREATE TABLE category
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    category_name VARCHAR(50) NOT NULL COMMENT '分类名称',
    create_time   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted    TINYINT  DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除'
);

-- 文章表
CREATE TABLE article
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    user_id     BIGINT       NOT NULL COMMENT '用户ID',
    category_id BIGINT       NOT NULL COMMENT '分类ID',
    title       VARCHAR(200) NOT NULL COMMENT '标题',
    content     TEXT         NOT NULL COMMENT '内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted  TINYINT  DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除'
);

-- 评论表
CREATE TABLE comment
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    user_id     BIGINT       NOT NULL COMMENT '用户ID',
    article_id  BIGINT       NOT NULL COMMENT '文章ID',
    content     VARCHAR(500) NOT NULL COMMENT '评论内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted  TINYINT  DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除'
);