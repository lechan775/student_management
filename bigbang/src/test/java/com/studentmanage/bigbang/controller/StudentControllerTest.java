package com.studentmanage.bigbang.controller;

import com.studentmanage.bigbang.model.dto.StudentDTO;
import com.studentmanage.bigbang.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * StudentController 集成测试
 * 需要 MySQL + Redis 运行。通过 docker-compose 启动后执行。
 */
@Disabled("需要 MySQL 运行，CI 中通过 docker-compose 提供")
@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private StudentService studentService;

    @Test
    @DisplayName("GET /api/students → 200 (分页)")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void listStudentsReturns200() throws Exception {
        Page<StudentDTO> page = new PageImpl<>(Collections.emptyList());
        when(studentService.listAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/students?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("POST /api/students → 需要 ADMIN 或 TEACHER 角色")
    @WithMockUser(username = "student1", roles = "STUDENT")
    void addStudentForbiddenForStudent() throws Exception {
        StudentDTO dto = new StudentDTO();
        dto.setStudentId("2024001");
        dto.setName("test");
        dto.setAge(20);
        dto.setSex("男");

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }
}
