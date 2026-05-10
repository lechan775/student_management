package com.studentmanage.ui;

import com.studentmanage.service.AuthService;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * 登录/注册/忘记密码 控制台界面 —— 进阶版
 * 保留原有交互逻辑，底层对接 AuthService（BCrypt哈希 + SQLite）
 */
public class LoginMenu {
    private final Scanner sc = new Scanner(System.in);

    /** 主菜单循环 */
    public void run() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║     Welcome to Student System      ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║  1. 登录 (Login)                   ║");
            System.out.println("║  2. 注册 (Register)                ║");
            System.out.println("║  3. 忘记密码 (Forgot Password)      ║");
            System.out.println("║  4. 退出 (Exit)                    ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print(">>> ");
            String choose = sc.next();

            switch (choose) {
                case "1" -> doLogin();
                case "2" -> doRegister();
                case "3" -> doForgotPassword();
                case "4" -> {
                    System.out.println("感谢使用，再见！");
                    return;
                }
                default -> System.out.println("无效选项，请重新输入！");
            }
        }
    }

    // ==================== 登录 ====================

    private void doLogin() {
        System.out.print("用户名: ");
        String username = sc.next();
        System.out.print("密码: ");
        String password = sc.next();

        // 生成验证码（保留原有逻辑）
        String code = generateCaptcha();
        System.out.println("验证码: " + code);
        System.out.print("请输入验证码: ");
        String inputCode = sc.next();

        if (!code.equalsIgnoreCase(inputCode)) {
            System.out.println("验证码错误！");
            return;
        }

        if (AuthService.login(username, password)) {
            System.out.println("登录成功！欢迎 " + username);
            // 进入学生管理子系统
            new StudentMenu(sc).run();
        } else {
            System.out.println("用户名或密码错误！");
        }
    }

    // ==================== 注册 ====================

    private void doRegister() {
        System.out.println("\n==== 用户注册 ====");

        // 1. 用户名
        String username;
        while (true) {
            System.out.print("用户名 (3~15位字母+数字，必须含字母): ");
            username = sc.next();
            if (!checkUsername(username)) {
                System.out.println("格式不合法，请重新输入！");
                continue;
            }
            break;
        }

        // 2. 密码
        String password;
        while (true) {
            System.out.print("密码: ");
            password = sc.next();
            System.out.print("确认密码: ");
            String again = sc.next();
            if (!password.equals(again)) {
                System.out.println("两次密码不一致！");
                continue;
            }
            break;
        }

        // 3. 身份证号
        String personId;
        while (true) {
            System.out.print("身份证号 (18位): ");
            personId = sc.next();
            if (!checkPersonId(personId)) {
                System.out.println("身份证号不合法！");
                continue;
            }
            break;
        }

        // 4. 手机号
        String phone;
        while (true) {
            System.out.print("手机号 (11位): ");
            phone = sc.next();
            if (!checkPhone(phone)) {
                System.out.println("手机号不合法！");
                continue;
            }
            break;
        }

        String result = AuthService.register(username, password, personId, phone);
        System.out.println(result);
    }

    // ==================== 忘记密码 ====================

    private void doForgotPassword() {
        System.out.println("\n==== 找回密码 ====");
        System.out.print("用户名: ");
        String username = sc.next();
        System.out.print("身份证号: ");
        String personId = sc.next();
        System.out.print("手机号: ");
        String phone = sc.next();
        System.out.print("新密码: ");
        String pwd1 = sc.next();
        System.out.print("确认新密码: ");
        String pwd2 = sc.next();

        String result = AuthService.resetPassword(username, personId, phone, pwd1, pwd2);
        System.out.println(result);
    }

    // ==================== 验证码 ====================

    /** 生成 5 位随机验证码（与原版逻辑一致） */
    private String generateCaptcha() {
        ArrayList<Character> arr = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            arr.add((char) ('a' + i));
            arr.add((char) ('A' + i));
        }
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            sb.append(arr.get(r.nextInt(arr.size())));
        }
        // 在随机位置插入一个数字
        int num = r.nextInt(10);
        int pos = r.nextInt(5);
        sb.insert(pos, (char) ('0' + num));

        return sb.toString();
    }

    // ==================== 输入校验 ====================

    private boolean checkUsername(String username) {
        int len = username.length();
        if (len < 3 || len > 15) return false;
        boolean hasLetter = false;
        for (int i = 0; i < len; i++) {
            char c = username.charAt(i);
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                hasLetter = true;
            } else if (!(c >= '0' && c <= '9')) {
                return false;
            }
        }
        return hasLetter;
    }

    private boolean checkPersonId(String id) {
        if (id.length() != 18) return false;
        if (id.startsWith("0")) return false;
        for (int i = 1; i < 17; i++) {
            if (!Character.isDigit(id.charAt(i))) return false;
        }
        char last = id.charAt(17);
        return Character.isDigit(last) || last == 'x' || last == 'X';
    }

    private boolean checkPhone(String phone) {
        if (phone.length() != 11) return false;
        for (char c : phone.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
}
