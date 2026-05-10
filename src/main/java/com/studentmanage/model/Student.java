package com.studentmanage.model;

/**
 * 学生实体类 —— 进阶版
 * 在"新手村"的 id/name/age/sex 基础上新增：
 *   - department  (院系)
 *   - className   (班级)
 *   - email       (邮箱)
 *   - phone       (手机号)
 */
public class Student {
    private String studentId;
    private String name;
    private int age;
    private String sex;
    private String department;   // 新增：院系
    private String className;    // 新增：班级
    private String email;        // 新增：邮箱
    private String phone;        // 新增：手机号

    public Student() {}

    // 兼容旧版构造器
    public Student(String studentId, String name, int age, String sex) {
        this(studentId, name, age, sex, "", "", "", "");
    }

    public Student(String studentId, String name, int age, String sex,
                   String department, String className, String email, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.department = department;
        this.className = className;
        this.email = email;
        this.phone = phone;
    }

    // ========== Getters & Setters ==========

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return String.format("Student{id='%s', name='%s', age=%d, sex='%s', dept='%s', class='%s'}",
                studentId, name, age, sex, department, className);
    }
}
