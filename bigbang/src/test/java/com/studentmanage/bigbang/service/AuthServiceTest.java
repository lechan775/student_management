package com.studentmanage.bigbang.service;

import com.studentmanage.bigbang.exception.BusinessException;
import com.studentmanage.bigbang.model.dto.LoginRequest;
import com.studentmanage.bigbang.model.dto.LoginResponse;
import com.studentmanage.bigbang.model.dto.RegisterRequest;
import com.studentmanage.bigbang.model.entity.RefreshToken;
import com.studentmanage.bigbang.model.entity.User;
import com.studentmanage.bigbang.model.enums.RoleEnum;
import com.studentmanage.bigbang.repository.RefreshTokenRepository;
import com.studentmanage.bigbang.repository.UserRepository;
import com.studentmanage.bigbang.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepo;
    @Mock private RefreshTokenRepository refreshTokenRepo;
    @Mock private PasswordEncoder encoder;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPasswordHash("$2a$10$hashedpassword");
        testUser.setPersonId("110101199001010001");
        testUser.setPhoneNumber("13800000001");
        testUser.setRole(RoleEnum.STUDENT);
        testUser.setEnabled(true);
    }

    @Test
    @DisplayName("登录成功 → 返回双 Token")
    void loginSuccess() {
        LoginRequest req = new LoginRequest();
        req.setUsername("testuser");
        req.setPassword("password123");

        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(encoder.matches("password123", testUser.getPasswordHash())).thenReturn(true);
        when(jwtUtil.generateAccessToken("testuser", "STUDENT")).thenReturn("access-token-mock");
        when(jwtUtil.generateRefreshToken("testuser", "STUDENT")).thenReturn("refresh-token-mock");
        when(refreshTokenRepo.save(any())).thenReturn(new RefreshToken());

        LoginResponse resp = authService.login(req);

        assertNotNull(resp);
        assertEquals("access-token-mock", resp.getAccessToken());
        assertEquals("refresh-token-mock", resp.getRefreshToken());
        assertEquals("testuser", resp.getUsername());
        assertEquals("STUDENT", resp.getRole());
    }

    @Test
    @DisplayName("登录失败：密码错误")
    void loginFailPasswordWrong() {
        LoginRequest req = new LoginRequest();
        req.setUsername("testuser");
        req.setPassword("wrongpassword");

        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(encoder.matches("wrongpassword", testUser.getPasswordHash())).thenReturn(false);

        assertThrows(BusinessException.class, () -> authService.login(req));
    }

    @Test
    @DisplayName("注册成功")
    void registerSuccess() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("newuser");
        req.setPassword("password123");
        req.setPersonId("110101199001010002");
        req.setPhoneNumber("13900000001");

        when(userRepo.existsByUsername("newuser")).thenReturn(false);
        when(encoder.encode("password123")).thenReturn("hashed");
        when(userRepo.save(any())).thenReturn(testUser);

        assertDoesNotThrow(() -> authService.register(req));
        verify(userRepo).save(any(User.class));
    }

    @Test
    @DisplayName("注册失败：用户名已存在")
    void registerFailDuplicate() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("existinguser");
        req.setPassword("password123");
        req.setPersonId("110101199001010002");
        req.setPhoneNumber("13900000001");

        when(userRepo.existsByUsername("existinguser")).thenReturn(true);

        assertThrows(BusinessException.class, () -> authService.register(req));
    }
}
