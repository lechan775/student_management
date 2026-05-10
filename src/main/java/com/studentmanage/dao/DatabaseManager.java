package com.studentmanage.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQLite 数据库管理器
 * 负责：建立连接、创建表结构
 * 数据库文件 student_management.db 自动生成在项目根目录
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:student_management.db";
    private static Connection connection;

    /** 获取数据库连接（懒加载单例） */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            initTables();
        }
        return connection;
    }

    /** 建表（IF NOT EXISTS 保证幂等） */
    private static void initTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {

            // 用户表
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    username     TEXT PRIMARY KEY,
                    password_hash TEXT NOT NULL,
                    person_id    TEXT NOT NULL,
                    phone_number TEXT NOT NULL
                )
            """);

            // 学生表：字段比"新手村"更丰富
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS students (
                    student_id  TEXT PRIMARY KEY,
                    name        TEXT NOT NULL,
                    age         INTEGER NOT NULL,
                    sex         TEXT NOT NULL,
                    department  TEXT DEFAULT '',
                    class_name  TEXT DEFAULT '',
                    email       TEXT DEFAULT '',
                    phone       TEXT DEFAULT ''
                )
            """);
        }
    }

    /** 关闭数据库连接 */
    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("[DB] 关闭连接失败: " + e.getMessage());
        }
    }
}
