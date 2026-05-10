package com.studentmanage.universe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生实体 — 宇宙版（8 字段）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false, unique = true, length = 30)
    private String studentId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, length = 4)
    private String sex;

    @Column(length = 50)
    private String department;

    @Column(name = "class_name", length = 50)
    private String className;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;
}
