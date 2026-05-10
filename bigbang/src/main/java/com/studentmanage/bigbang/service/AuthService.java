package com.studentmanage.bigbang.service;

import com.studentmanage.bigbang.exception.BusinessException;
import com.studentmanage.bigbang.model.dto.*;
import com.studentmanage.bigbang.model.entity.RefreshToken;
import com.studentmanage.bigbang.model.entity.User;
import com.studentmanage.bigbang.model.enums.RoleEnum;
import com.studentmanage.bigbang.repository.RefreshTokenRepository;
import com.studentmanage.bigbang.repository.UserRepository;
import com.studentmanage.bigbang.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final RefreshTokenRepository refreshTokenRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepo, RefreshTokenRepository refreshTokenRepo,
                       PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.refreshTokenRepo = refreshTokenRepo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    // ==================== 登录 ====================

    @Transactional
    public LoginResponse login(LoginRequest req) {
        User user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));
        if (!encoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (!user.getEnabled()) {
            throw new BusinessException("账号已被禁用");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getRole().name());

        // 持久化 refresh token
        RefreshToken rt = new RefreshToken();
        rt.setUsername(user.getUsername());
        rt.setToken(refreshToken);
        rt.setExpiredAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepo.save(rt);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    // ==================== 注册 ====================

    @Transactional
    public void register(RegisterRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new BusinessException("该用户名已被注册");
        }
        validatePersonId(req.getPersonId());
        validatePhone(req.getPhoneNumber());

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPasswordHash(encoder.encode(req.getPassword()));
        user.setPersonId(req.getPersonId());
        user.setPhoneNumber(req.getPhoneNumber());
        user.setRole(RoleEnum.STUDENT);
        userRepo.save(user);
    }

    // ==================== 忘记密码 ====================

    @Transactional
    public void resetPassword(String username, String personId, String phone, String newPassword) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (!user.getPersonId().equalsIgnoreCase(personId)) {
            throw new BusinessException("身份证号不匹配");
        }
        if (!user.getPhoneNumber().equals(phone)) {
            throw new BusinessException("手机号不匹配");
        }
        user.setPasswordHash(encoder.encode(newPassword));
        userRepo.save(user);
    }

    // ==================== Token 刷新 ====================

    @Transactional
    public LoginResponse refreshToken(String refreshTokenStr) {
        // 1. 验证 token 有效性
        if (!jwtUtil.validateToken(refreshTokenStr)) {
            throw new BusinessException(401, "refresh token 无效或已过期");
        }
        // 2. 检查数据库中是否存在
        RefreshToken rt = refreshTokenRepo.findByToken(refreshTokenStr)
                .orElseThrow(() -> new BusinessException(401, "refresh token 已失效"));
        if (rt.getExpiredAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepo.delete(rt);
            throw new BusinessException(401, "refresh token 已过期");
        }
        // 3. 删除旧 refresh token（旋转策略）
        refreshTokenRepo.delete(rt);

        String username = jwtUtil.extractUsername(refreshTokenStr);
        String role = jwtUtil.extractRole(refreshTokenStr);

        String newAccess = jwtUtil.generateAccessToken(username, role);
        String newRefresh = jwtUtil.generateRefreshToken(username, role);

        RefreshToken newRt = new RefreshToken();
        newRt.setUsername(username);
        newRt.setToken(newRefresh);
        newRt.setExpiredAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepo.save(newRt);

        return LoginResponse.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .username(username)
                .role(role)
                .build();
    }

    // ==================== 当前用户信息 ====================

    public Map<String, Object> getCurrentUser(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        Map<String, Object> info = new HashMap<>();
        info.put("username", user.getUsername());
        info.put("role", user.getRole().name());
        info.put("personId", user.getPersonId());
        info.put("phoneNumber", user.getPhoneNumber());
        info.put("avatarUrl", user.getAvatarUrl());
        return info;
    }

    // ==================== 输入校验 ====================

    private void validatePersonId(String id) {
        if (id == null || id.length() != 18) {
            throw new BusinessException("身份证号必须为18位");
        }
        for (int i = 0; i < 17; i++) {
            if (!Character.isDigit(id.charAt(i))) {
                throw new BusinessException("身份证号前17位必须为数字");
            }
        }
        char last = id.charAt(17);
        if (!Character.isDigit(last) && last != 'x' && last != 'X') {
            throw new BusinessException("身份证号最后一位必须为数字或X");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.length() != 11) {
            throw new BusinessException("手机号必须为11位");
        }
        for (char c : phone.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new BusinessException("手机号必须全为数字");
            }
        }
    }
}
