package com.studentmanage.ui;

import com.studentmanage.model.Student;
import com.studentmanage.service.StudentService;

import java.util.Scanner;

/**
 * 学生管理控制台界面 —— 进阶版
 * 在"新手村"增删查改遍历基础上新增：
 *   - 按姓名搜索（模糊匹配）
 *   - 按院系过滤
 *   - 字段扩展为 8 项（id/name/age/sex/dept/class/email/phone）
 */
public class StudentMenu {
    private final Scanner sc;

    public StudentMenu(Scanner sc) {
        this.sc = sc;
    }

    public void run() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║       学生信息管理系统 (进阶版)     ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║  1. 添加学生                       ║");
            System.out.println("║  2. 删除学生                       ║");
            System.out.println("║  3. 查询学生 (按学号)              ║");
            System.out.println("║  4. 搜索学生 (按姓名)  【新增】    ║");
            System.out.println("║  5. 过滤学生 (按院系)  【新增】    ║");
            System.out.println("║  6. 更新学生信息                   ║");
            System.out.println("║  7. 显示全部学生                   ║");
            System.out.println("║  8. 学生总数统计       【新增】    ║");
            System.out.println("║  9. 返回上级菜单                   ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print(">>> ");

            int choice;
            try {
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("请输入数字！");
                continue;
            }

            switch (choice) {
                case 1 -> doAdd();
                case 2 -> doDelete();
                case 3 -> doQueryById();
                case 4 -> doQueryByName();
                case 5 -> doQueryByDept();
                case 6 -> doUpdate();
                case 7 -> doListAll();
                case 8 -> doCount();
                case 9 -> { return; }
                default -> System.out.println("无效选项，请重新输入！");
            }
        }
    }

    // ==================== 添加学生 ====================

    private void doAdd() {
        System.out.println("\n==== 添加学生 ====");
        System.out.print("学号: ");
        String id = sc.next();
        System.out.print("姓名: ");
        String name = sc.next();
        System.out.print("年龄: ");
        int age = safeNextInt();
        System.out.print("性别 (男/女): ");
        String sex = sc.next();

        // 新增字段
        System.out.print("院系: ");
        String dept = sc.next();
        System.out.print("班级: ");
        String cls = sc.next();
        System.out.print("邮箱: ");
        String email = sc.next();
        System.out.print("手机: ");
        String phone = sc.next();

        Student s = new Student(id, name, age, sex, dept, cls, email, phone);
        System.out.println(StudentService.addStudent(s));
    }

    // ==================== 删除学生 ====================

    private void doDelete() {
        System.out.println("\n==== 删除学生 ====");
        System.out.print("请输入要删除的学号: ");
        String id = sc.next();
        System.out.println(StudentService.deleteStudent(id));
    }

    // ==================== 查询（按学号） ====================

    private void doQueryById() {
        System.out.println("\n==== 查询学生 (学号) ====");
        System.out.print("请输入学号: ");
        String id = sc.next();
        System.out.println(StudentService.queryById(id));
    }

    // ==================== 搜索（按姓名，进阶功能） ====================

    private void doQueryByName() {
        System.out.println("\n==== 搜索学生 (姓名-模糊匹配) ====");
        System.out.print("请输入姓名关键字: ");
        String keyword = sc.next();
        System.out.println(StudentService.queryByName(keyword));
    }

    // ==================== 过滤（按院系，进阶功能） ====================

    private void doQueryByDept() {
        System.out.println("\n==== 过滤学生 (院系) ====");
        System.out.print("请输入院系关键字: ");
        String dept = sc.next();
        System.out.println(StudentService.queryByDepartment(dept));
    }

    // ==================== 更新学生 ====================

    private void doUpdate() {
        System.out.println("\n==== 更新学生信息 ====");
        System.out.print("请输入要更新的学号: ");
        String id = sc.next();

        // 先查一下是否存在
        Student old = com.studentmanage.dao.StudentDAO.findById(id);
        if (old == null) {
            System.out.println("未找到学号为 " + id + " 的学生。");
            return;
        }

        System.out.print("新姓名 (" + old.getName() + "): ");
        String name = sc.next();
        System.out.print("新年龄 (" + old.getAge() + "): ");
        int age = safeNextInt();
        System.out.print("新性别 (" + old.getSex() + "): ");
        String sex = sc.next();
        System.out.print("新院系 (" + old.getDepartment() + "): ");
        String dept = sc.next();
        System.out.print("新班级 (" + old.getClassName() + "): ");
        String cls = sc.next();
        System.out.print("新邮箱 (" + old.getEmail() + "): ");
        String email = sc.next();
        System.out.print("新手机 (" + old.getPhone() + "): ");
        String phone = sc.next();

        Student s = new Student(id, name, age, sex, dept, cls, email, phone);
        System.out.println(StudentService.updateStudent(s));
    }

    // ==================== 遍历全部 ====================

    private void doListAll() {
        System.out.println(StudentService.listAll());
    }

    // ==================== 统计 ====================

    private void doCount() {
        int count = StudentService.countStudents();
        System.out.println("当前学生总数: " + count);
    }

    // ==================== 工具方法 ====================

    private int safeNextInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.print("请输入有效数字: ");
            }
        }
    }
}
