-- 清空所有题目数据并重置自增主键

-- 清空题目表
DELETE FROM question;

-- 重置班级题目计数
UPDATE chaoxingClass SET question_total = 0;

-- 重置自增主键（下一条从 1 开始）
ALTER SEQUENCE question_id_seq RESTART WITH 1;
