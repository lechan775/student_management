package com.studentmanage.bigbang.model.enums;

public enum RoleEnum {
    ADMIN,    // 管理员：全部权限
    TEACHER,  // 教师：管理学生 + 查看仪表盘
    STUDENT   // 学生：只读学生列表
}
