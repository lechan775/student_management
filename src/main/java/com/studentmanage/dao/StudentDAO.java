package com.studentmanage.dao;

import com.studentmanage.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生数据访问对象 —— 进阶版
 * SQLite 持久化存储，支持全量 CRUD + 模糊搜索
 */
public class StudentDAO {

    // ==================== 增 ====================

    /** 添加学生 */
    public static boolean insert(Student s) {
        String sql = "INSERT INTO students (student_id, name, age, sex, department, class_name, email, phone) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getStudentId());
            ps.setString(2, s.getName());
            ps.setInt(3, s.getAge());
            ps.setString(4, s.getSex());
            ps.setString(5, s.getDepartment());
            ps.setString(6, s.getClassName());
            ps.setString(7, s.getEmail());
            ps.setString(8, s.getPhone());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[StudentDAO] insert error: " + e.getMessage());
            return false;
        }
    }

    // ==================== 删 ====================

    /** 按学号删除 */
    public static boolean deleteById(String studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[StudentDAO] deleteById error: " + e.getMessage());
            return false;
        }
    }

    // ==================== 查 ====================

    /** 按学号精确查询 */
    public static Student findById(String studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rowToStudent(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] findById error: " + e.getMessage());
        }
        return null;
    }

    /** 按姓名模糊搜索（进阶功能） */
    public static List<Student> searchByName(String keyword) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE name LIKE ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rowToStudent(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] searchByName error: " + e.getMessage());
        }
        return list;
    }

    /** 按院系过滤（进阶功能） */
    public static List<Student> searchByDepartment(String dept) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE department LIKE ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + dept + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rowToStudent(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] searchByDepartment error: " + e.getMessage());
        }
        return list;
    }

    /** 检查学号是否已存在 */
    public static boolean existsById(String studentId) {
        return findById(studentId) != null;
    }

    /** 获取全部学生列表 */
    public static List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_id";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rowToStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] findAll error: " + e.getMessage());
        }
        return list;
    }

    /** 获取学生总数 */
    public static int count() {
        String sql = "SELECT COUNT(*) FROM students";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] count error: " + e.getMessage());
        }
        return 0;
    }

    // ==================== 改 ====================

    /** 全字段更新学生信息（按学号） */
    public static boolean update(Student s) {
        String sql = "UPDATE students SET name=?, age=?, sex=?, department=?, class_name=?, email=?, phone=? " +
                     "WHERE student_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setInt(2, s.getAge());
            ps.setString(3, s.getSex());
            ps.setString(4, s.getDepartment());
            ps.setString(5, s.getClassName());
            ps.setString(6, s.getEmail());
            ps.setString(7, s.getPhone());
            ps.setString(8, s.getStudentId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[StudentDAO] update error: " + e.getMessage());
            return false;
        }
    }

    // ==================== 工具方法 ====================

    private static Student rowToStudent(ResultSet rs) throws SQLException {
        return new Student(
                rs.getString("student_id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("sex"),
                rs.getString("department"),
                rs.getString("class_name"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }
}
