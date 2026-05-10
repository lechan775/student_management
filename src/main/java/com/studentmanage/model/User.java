package com.studentmanage.model;

/**
 * 用户实体类 —— 进阶版
 * 字段与原"新手村"版本保持一致：用户名、密码、身份证号、手机号
 * 新增：加盐哈希后的密码存储（不再存明文）
 */
public class User {
    private String username;
    private String passwordHash;  // BCrypt 哈希，非明文
    private String personId;      // 身份证号
    private String phoneNumber;   // 手机号

    public User() {}

    public User(String username, String passwordHash, String personId, String phoneNumber) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.personId = personId;
        this.phoneNumber = phoneNumber;
    }

    // ========== Getters & Setters ==========

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return String.format("User{username='%s', personId='%s', phoneNumber='%s'}",
                username, personId, phoneNumber);
    }
}
