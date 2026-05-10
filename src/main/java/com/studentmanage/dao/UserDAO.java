package com.studentmanage.dao;

import com.studentmanage.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问对象 —— 进阶版
 * 通过 SQLite 实现持久化存储，替代原 ArrayList 内存方案
 */
public class UserDAO {

    // ==================== 增 ====================

    /** 插入新用户（注册） */
    public static boolean insert(User user) {
        String sql = "INSERT INTO users (username, password_hash, person_id, phone_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getPersonId());
            ps.setString(4, user.getPhoneNumber());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDAO] insert error: " + e.getMessage());
            return false;
        }
    }

    // ==================== 查 ====================

    /** 按用户名查询（登录用） */
    public static User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rowToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] findByUsername error: " + e.getMessage());
        }
        return null;
    }

    /** 检查用户名是否已存在 */
    public static boolean existsByUsername(String username) {
        return findByUsername(username) != null;
    }

    /** 获取全量用户列表（调试用） */
    public static List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rowToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] findAll error: " + e.getMessage());
        }
        return list;
    }

    // ==================== 改 ====================

    /** 修改密码（忘记密码功能） */
    public static boolean updatePassword(String username, String newPasswordHash) {
        String sql = "UPDATE users SET password_hash = ? WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPasswordHash);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDAO] updatePassword error: " + e.getMessage());
            return false;
        }
    }

    // ==================== 工具方法 ====================

    private static User rowToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("username"),
                rs.getString("password_hash"),
                rs.getString("person_id"),
                rs.getString("phone_number")
        );
    }
}
