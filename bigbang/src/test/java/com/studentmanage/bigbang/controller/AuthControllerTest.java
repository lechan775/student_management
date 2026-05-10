package com.studentmanage.bigbang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentmanage.bigbang.model.dto.LoginRequest;
import com.studentmanage.bigbang.model.dto.LoginResponse;
import com.studentmanage.bigbang.service.AuthService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * AuthController 集成测试
 * 需要 MySQL + Redis 运行。通过 docker-compose 启动后执行。
 * 本地开发: mvn test -pl . -Dtest=AuthControllerTest
 */
@Disabled("需要 MySQL 运行，CI 中通过 docker-compose 提供")
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private AuthService authService;

    @Test
    @DisplayName("POST /api/auth/login → 200")
    void loginReturns200() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setUsername("admin");
        req.setPassword("password");

        when(authService.login(any())).thenReturn(
                LoginResponse.builder().accessToken("mock-access").refreshToken("mock-refresh")
                        .username("admin").role("ADMIN").build());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.accessToken").value("mock-access"));
    }

    @Test
    @DisplayName("GET /api/auth/me → 需认证")
    void meRequiresAuth() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/auth/me → 带 Mock 用户")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void meWithAuth() throws Exception {
        when(authService.getCurrentUser("admin")).thenReturn(java.util.Map.of("username", "admin"));

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("admin"));
    }
}
