package com.studentmanage.bigbang.config;

import com.studentmanage.bigbang.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 公开接口
                .requestMatchers("/api/auth/login", "/api/auth/register",
                        "/api/auth/reset-password", "/api/auth/refresh").permitAll()
                // Swagger & Actuator
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
                        "/doc.html", "/webjars/**").permitAll()
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                // 静态资源 (前后端分离时Nginx处理，这里兜底)
                .requestMatchers("/static/**", "/index.html", "/favicon.ico").permitAll()
                // 学生管理: STUDENT 只读
                .requestMatchers(HttpMethod.GET, "/api/students/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                .requestMatchers("/api/students/**").hasAnyRole("ADMIN", "TEACHER")
                // 仪表盘 & 导出: ADMIN + TEACHER
                .requestMatchers("/api/dashboard/**", "/api/export/**").hasAnyRole("ADMIN", "TEACHER")
                // 文件上传: 登录用户均可
                .requestMatchers("/api/files/**").authenticated()
                // 日志: 仅 ADMIN
                .requestMatchers("/api/logs/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
