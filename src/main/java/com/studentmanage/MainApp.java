package com.studentmanage;

import com.studentmanage.dao.DatabaseManager;
import com.studentmanage.ui.LoginMenu;

/**
 * 主入口 —— 进阶版
 *
 * 用法：
 *   1. mvn compile exec:java -Dexec.mainClass="com.studentmanage.MainApp"
 *   2. 或打包后：java -jar target/student-management-2.0.0-advanced-jar-with-dependencies.jar
 *
 * 相比"新手村"版本的核心升级：
 *   - SQLite 数据库持久化（不再丢数据）
 *   - BCrypt 密码哈希（不再存明文）
 *   - MVC 分层架构（model/dao/service/ui）
 *   - 学生字段从 4 项扩展到 8 项
 *   - 新增按姓名模糊搜索、按院系过滤、学生总数统计
 */
public class MainApp {
    public static void main(String[] args) {
        // 注册 JVM 关闭钩子：确保数据库连接正常关闭
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::close));

        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║  学生信息管理系统 v2.0 — 进阶版       ║");
        System.out.println("║  SQLite + BCrypt | 持久化 + 安全哈希  ║");
        System.out.println("╚═══════════════════════════════════════╝");

        new LoginMenu().run();

        DatabaseManager.close();
        System.out.println("[系统] 数据库连接已关闭，再见。");
    }
}
