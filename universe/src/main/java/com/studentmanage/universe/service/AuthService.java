package com.studentmanage.universe.service;

import com.studentmanage.universe.dto.LoginRequest;
import com.studentmanage.universe.dto.RegisterRequest;
import com.studentmanage.universe.model.User;
import com.studentmanage.universe.repository.UserRepository;
import com.studentmanage.universe.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final LogService logService;

    public AuthService(UserRepository userRepo, PasswordEncoder encoder,
                       JwtUtil jwtUtil, LogService logService) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.logService = logService;
    }

    /** 登录 → 返回 JWT */
    public String login(LoginRequest req) {
        User user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        if (!encoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("用户名或密码错误");
        }
        logService.log(user.getUsername(), "LOGIN", "用户登录成功");
        return jwtUtil.generateToken(user.getUsername(), user.getRole().name());
    }

    /** 注册 */
    public void register(RegisterRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new RuntimeException("该用户名已被注册");
        }
        // 身份证 & 手机号校验（保持新手村逻辑）
        validatePersonId(req.getPersonId());
        validatePhone(req.getPhoneNumber());

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPasswordHash(encoder.encode(req.getPassword()));
        user.setPersonId(req.getPersonId());
        user.setPhoneNumber(req.getPhoneNumber());
        user.setRole(User.Role.STUDENT);
        userRepo.save(user);
        logService.log(user.getUsername(), "REGISTER", "新用户注册");
    }

    /** 忘记密码 */
    public void resetPassword(String username, String personId, String phone, String newPassword) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        if (!user.getPersonId().equalsIgnoreCase(personId)) {
            throw new RuntimeException("身份证号不匹配");
        }
        if (!user.getPhoneNumber().equals(phone)) {
            throw new RuntimeException("手机号不匹配");
        }
        user.setPasswordHash(encoder.encode(newPassword));
        userRepo.save(user);
        logService.log(username, "RESET_PASSWORD", "密码已重置");
    }

    /** 获取当前用户信息 */
    public Map<String, Object> getUserInfo(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        Map<String, Object> info = new HashMap<>();
        info.put("username", user.getUsername());
        info.put("role", user.getRole().name());
        info.put("personId", maskIdCard(user.getPersonId()));
        info.put("phoneNumber", maskPhone(user.getPhoneNumber()));
        return info;
    }

    private void validatePersonId(String id) {
        if (id == null || id.length() != 18 || id.startsWith("0"))
            throw new RuntimeException("身份证号格式不合法");
        for (int i = 1; i < 17; i++) {
            if (!Character.isDigit(id.charAt(i)))
                throw new RuntimeException("身份证号格式不合法");
        }
        char last = id.charAt(17);
        if (!Character.isDigit(last) && last != 'x' && last != 'X')
            throw new RuntimeException("身份证号格式不合法");
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.length() != 11)
            throw new RuntimeException("手机号格式不合法");
    }

    private String maskIdCard(String id) {
        return id.substring(0, 3) + "****" + id.substring(15);
    }

    private String maskPhone(String phone) {
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}
