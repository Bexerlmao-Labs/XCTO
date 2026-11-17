-- XCTO 数据库初始化
-- 执行方式：docker exec -i xcto-db psql -U postgres < 01-create-tables.sql

CREATE DATABASE xcto;
\c xcto

-- 用户表（sa-token 登录主体）
CREATE TABLE IF NOT EXISTS sys_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,                 -- 用户名
    password_hash VARCHAR(64) NOT NULL,                   -- SHA-256 加密后的密码（64 位十六进制）
    role VARCHAR(20) NOT NULL DEFAULT 'user',             -- 角色：admin / user
    courses JSONB NOT NULL DEFAULT '[]',                  -- 拥有的课程 classId 列表
    upload_key VARCHAR(64),                               -- 上传密钥（预留字段，当前上传鉴权走 sa-token，可为空）
    upload_balance INT NOT NULL DEFAULT 0,                -- 上传次数余额（剩余可上传次数）
    upload_used BIGINT NOT NULL DEFAULT 0,                -- 累计已上传次数（审计用）
    status SMALLINT NOT NULL DEFAULT 1,                   -- 状态：1 启用 / 0 禁用
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_time TIMESTAMP                             -- 最后登录时间
);
CREATE INDEX IF NOT EXISTS idx_sys_user_username ON sys_user (username);

-- 课程表
CREATE TABLE IF NOT EXISTS chaoxingClass (
    id SERIAL PRIMARY KEY,
    class_id BIGINT,
    class_name VARCHAR(255),
    question_total BIGINT,
    create_user BIGINT,                                   -- 创建该课程题库的用户 ID（sys_user.id）
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_class_id ON chaoxingClass (class_id);
CREATE INDEX IF NOT EXISTS idx_class_create_user ON chaoxingClass (create_user);

-- 题目表
CREATE TABLE IF NOT EXISTS question (
    id SERIAL PRIMARY KEY,
    class_id BIGINT,
    question_type SMALLINT,
    question TEXT,
    options TEXT,
    answer TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_question_class_id ON question(class_id);
