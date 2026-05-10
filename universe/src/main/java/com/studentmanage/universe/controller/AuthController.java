package com.studentmanage.universe.controller;

import com.studentmanage.universe.dto.ApiResponse;
import com.studentmanage.universe.dto.LoginRequest;
import com.studentmanage.universe.dto.RegisterRequest;
import com.studentmanage.universe.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@RequestBody LoginRequest req) {
        try {
            String token = authService.login(req);
            return ApiResponse.success("登录成功", Map.of("token", token));
        } catch (RuntimeException e) {
            return ApiResponse.error(401, e.getMessage());
        }
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody RegisterRequest req) {
        try {
            authService.register(req);
            return ApiResponse.success("注册成功", null);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestBody Map<String, String> body) {
        try {
            authService.resetPassword(
                    body.get("username"),
                    body.get("personId"),
                    body.get("phone"),
                    body.get("newPassword")
            );
            return ApiResponse.success("密码重置成功", null);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> getCurrentUser(Principal principal) {
        return ApiResponse.success(authService.getUserInfo(principal.getName()));
    }
}
