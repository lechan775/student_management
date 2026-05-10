-- ============================================================
-- Flyway V1: 初始化建表
-- 宇宙爆炸版 — MySQL 8.0, utf8mb4
-- ============================================================

CREATE TABLE IF NOT EXISTS users (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    person_id     VARCHAR(18)  NOT NULL,
    phone_number  VARCHAR(11)  NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'STUDENT',
    avatar_url    VARCHAR(255),
    enabled       TINYINT(1)   NOT NULL DEFAULT 1,
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_username (username),
    INDEX idx_users_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS students (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id    VARCHAR(30)  NOT NULL UNIQUE,
    name          VARCHAR(50)  NOT NULL,
    age           INT          NOT NULL,
    sex           VARCHAR(4)   NOT NULL,
    department    VARCHAR(50)  DEFAULT '',
    class_name    VARCHAR(50)  DEFAULT '',
    email         VARCHAR(100) DEFAULT '',
    phone         VARCHAR(20)  DEFAULT '',
    avatar_url    VARCHAR(255),
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_students_name (name),
    INDEX idx_students_department (department),
    INDEX idx_students_sex (sex)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS operation_logs (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL,
    operation     VARCHAR(50)  NOT NULL,
    detail        TEXT,
    ip_address    VARCHAR(45),
    user_agent    VARCHAR(500),
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_logs_username (username),
    INDEX idx_logs_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS refresh_tokens (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL,
    token         VARCHAR(500) NOT NULL UNIQUE,
    expired_at    DATETIME     NOT NULL,
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_refresh_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
