package com.studentmanage.bigbang.controller;

import com.studentmanage.bigbang.model.dto.*;
import com.studentmanage.bigbang.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Tag(name = "认证管理", description = "登录/注册/忘记密码/Token刷新")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "用户登录", description = "返回 accessToken + refreshToken")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ApiResponse.success(authService.login(req));
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ApiResponse.success("注册成功", null);
    }

    @Operation(summary = "忘记密码")
    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestBody Map<String, String> body) {
        authService.resetPassword(
                body.get("username"), body.get("personId"),
                body.get("phone"), body.get("newPassword"));
        return ApiResponse.success("密码重置成功", null);
    }

    @Operation(summary = "刷新 Token", description = "用 refreshToken 换取新的双 Token")
    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(@Valid @RequestBody TokenRefreshRequest req) {
        return ApiResponse.success(authService.refreshToken(req.getRefreshToken()));
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> currentUser(Principal principal) {
        return ApiResponse.success(authService.getCurrentUser(principal.getName()));
    }
}
