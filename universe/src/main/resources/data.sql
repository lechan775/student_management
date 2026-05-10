-- 初始化管理员账号（密码 admin123 的 BCrypt 哈希）
MERGE INTO users (id, username, password_hash, person_id, phone_number, role)
    KEY(username)
    VALUES (1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
            '110101199001010001', '13800000000', 'ADMIN');
