package com.studentmanage.service;

import com.studentmanage.dao.UserDAO;
import com.studentmanage.model.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * 认证服务 —— 进阶版
 * 核心变更：BCrypt 哈希替代明文密码存储
 * 登录/注册/忘记密码 三项功能对应用户的 README 需求
 */
public class AuthService {

    /** 注册：校验 → 哈希密码 → 入库 */
    public static String register(String username, String password, String personId, String phoneNumber) {
        // 1. 校验用户名唯一性
        if (UserDAO.existsByUsername(username)) {
            return "该用户名已被注册！";
        }
        // 2. 哈希密码
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        // 3. 入库
        User user = new User(username, hash, personId, phoneNumber);
        boolean ok = UserDAO.insert(user);
        return ok ? "注册成功！" : "注册失败，请稍后重试。";
    }

    /** 登录：查用户 → 验密码 → 验验证码（验证码由 UI 层生成） */
    public static boolean login(String username, String password) {
        User user = UserDAO.findByUsername(username);
        if (user == null) {
            return false;  // 用户不存在
        }
        return BCrypt.checkpw(password, user.getPasswordHash());
    }

    /** 忘记密码：校验身份 → 更新密码哈希 */
    public static String resetPassword(String username, String personId, String phoneNumber,
                                       String newPassword1, String newPassword2) {
        User user = UserDAO.findByUsername(username);
        if (user == null) {
            return "该用户不存在！";
        }
        if (!user.getPersonId().equalsIgnoreCase(personId)) {
            return "身份证号不匹配！";
        }
        if (!user.getPhoneNumber().equals(phoneNumber)) {
            return "手机号不匹配！";
        }
        if (!newPassword1.equals(newPassword2)) {
            return "两次输入的密码不一致！";
        }
        String newHash = BCrypt.hashpw(newPassword1, BCrypt.gensalt());
        boolean ok = UserDAO.updatePassword(username, newHash);
        return ok ? "密码修改成功！" : "密码修改失败，请稍后重试。";
    }
}
