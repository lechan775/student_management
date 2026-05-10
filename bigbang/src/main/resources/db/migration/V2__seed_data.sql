-- ============================================================
-- Flyway V2: 种子数据
-- 初始管理员: admin / Admin@123
-- BCrypt($2a$10$...)
-- ============================================================

INSERT IGNORE INTO users (username, password_hash, person_id, phone_number, role)
VALUES ('admin', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36PQm4sEPhMNPfFhkJm3fTq',
        '110101199001010001', '13800000000', 'ADMIN');

INSERT IGNORE INTO students (student_id, name, age, sex, department, class_name, email, phone) VALUES
('2024001', '张三', 20, '男', '计算机科学与技术', '计科2401', 'zhangsan@example.com', '13900000001'),
('2024002', '李四', 21, '女', '软件工程', '软工2401', 'lisi@example.com', '13900000002'),
('2024003', '王五', 19, '男', '人工智能', '智能2401', 'wangwu@example.com', '13900000003'),
('2024004', '赵六', 22, '女', '计算机科学与技术', '计科2402', 'zhaoliu@example.com', '13900000004'),
('2024005', '孙七', 20, '男', '数据科学', '数据2401', 'sunqi@example.com', '13900000005');
