package com.studentmanage.service;

import com.studentmanage.dao.StudentDAO;
import com.studentmanage.model.Student;

import java.util.List;

/**
 * 学生管理服务 —— 进阶版
 * 在"新手村"的增删查改遍历基础上增强：
 *   - 多维度搜索（学号 / 姓名 / 院系）
 *   - 字段更丰富（院系、班级、邮箱、手机）
 *   - 返回操作结果字符串供 UI 层直接展示
 */
public class StudentService {

    // ==================== 增 ====================

    public static String addStudent(Student s) {
        if (StudentDAO.existsById(s.getStudentId())) {
            return "学号 " + s.getStudentId() + " 已存在，添加失败！";
        }
        boolean ok = StudentDAO.insert(s);
        return ok ? "学生 " + s.getName() + " 添加成功！" : "添加失败，请稍后重试。";
    }

    // ==================== 删 ====================

    public static String deleteStudent(String studentId) {
        boolean ok = StudentDAO.deleteById(studentId);
        return ok ? "学号 " + studentId + " 已删除。" : "未找到学号为 " + studentId + " 的学生。";
    }

    // ==================== 查（增强版） ====================

    /** 按学号查询 */
    public static String queryById(String studentId) {
        Student s = StudentDAO.findById(studentId);
        if (s == null) {
            return "未找到学号为 " + studentId + " 的学生。";
        }
        return formatSingle(s);
    }

    /** 按姓名模糊搜索（进阶功能） */
    public static String queryByName(String keyword) {
        List<Student> list = StudentDAO.searchByName(keyword);
        return formatList(list, "按姓名「" + keyword + "」搜索");
    }

    /** 按院系过滤（进阶功能） */
    public static String queryByDepartment(String dept) {
        List<Student> list = StudentDAO.searchByDepartment(dept);
        return formatList(list, "院系「" + dept + "」");
    }

    // ==================== 改 ====================

    public static String updateStudent(Student s) {
        if (!StudentDAO.existsById(s.getStudentId())) {
            return "学号 " + s.getStudentId() + " 不存在，无法更新。";
        }
        boolean ok = StudentDAO.update(s);
        return ok ? "学号 " + s.getStudentId() + " 信息已更新。" : "更新失败，请稍后重试。";
    }

    // ==================== 遍历 ====================

    public static String listAll() {
        List<Student> list = StudentDAO.findAll();
        return formatList(list, "全部学生");
    }

    // ==================== 统计 ====================

    public static int countStudents() {
        return StudentDAO.count();
    }

    // ==================== 格式化输出 ====================

    private static String formatSingle(Student s) {
        StringBuilder sb = new StringBuilder();
        sb.append("┌────────────────────────────────────────┐\n");
        sb.append(String.format("│ 学号  : %-30s │\n", s.getStudentId()));
        sb.append(String.format("│ 姓名  : %-30s │\n", s.getName()));
        sb.append(String.format("│ 年龄  : %-30d │\n", s.getAge()));
        sb.append(String.format("│ 性别  : %-30s │\n", s.getSex()));
        sb.append(String.format("│ 院系  : %-30s │\n", s.getDepartment()));
        sb.append(String.format("│ 班级  : %-30s │\n", s.getClassName()));
        sb.append(String.format("│ 邮箱  : %-30s │\n", s.getEmail()));
        sb.append(String.format("│ 手机  : %-30s │\n", s.getPhone()));
        sb.append("└────────────────────────────────────────┘");
        return sb.toString();
    }

    private static String formatList(List<Student> list, String title) {
        if (list.isEmpty()) {
            return title + "：无结果。";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(title).append("，共 ").append(list.size()).append(" 人：\n");
        // 表头
        sb.append(String.format("%-12s %-8s %4s %4s %-12s %-10s %-18s %-13s\n",
                "学号", "姓名", "年龄", "性别", "院系", "班级", "邮箱", "手机"));
        sb.append("-".repeat(85)).append("\n");
        for (Student s : list) {
            sb.append(String.format("%-12s %-8s %4d %4s %-12s %-10s %-18s %-13s\n",
                    s.getStudentId(),
                    truncate(s.getName(), 6),
                    s.getAge(),
                    s.getSex(),
                    truncate(s.getDepartment(), 10),
                    truncate(s.getClassName(), 8),
                    truncate(s.getEmail(), 16),
                    truncate(s.getPhone(), 11)));
        }
        return sb.toString();
    }

    private static String truncate(String s, int max) {
        if (s == null || s.isEmpty()) return "-";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}
