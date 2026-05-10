package com.studentmanage.bigbang.controller;

import com.studentmanage.bigbang.model.dto.ApiResponse;
import com.studentmanage.bigbang.model.dto.StudentDTO;
import com.studentmanage.bigbang.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "学生管理", description = "学生 CRUD + 多维度搜索（后端分页）")
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "获取学生列表（分页）")
    @GetMapping
    public ApiResponse<Page<StudentDTO>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "studentId") String sort) {
        return ApiResponse.success(studentService.listAll(
                PageRequest.of(page, size, Sort.by(sort).ascending())));
    }

    @Operation(summary = "搜索学生（姓名 + 院系联合筛选 + 分页）")
    @GetMapping("/search")
    public ApiResponse<Page<StudentDTO>> search(
            @Parameter(description = "姓名关键字") @RequestParam(required = false) String keyword,
            @Parameter(description = "院系关键字") @RequestParam(required = false) String dept,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(studentService.search(keyword, dept,
                PageRequest.of(page, size, Sort.by("studentId").ascending())));
    }

    @Operation(summary = "添加学生")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ApiResponse<StudentDTO> add(@Valid @RequestBody StudentDTO dto) {
        return ApiResponse.success("添加成功", studentService.addStudent(dto));
    }

    @Operation(summary = "更新学生")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ApiResponse<StudentDTO> update(@PathVariable Long id, @Valid @RequestBody StudentDTO dto) {
        return ApiResponse.success("更新成功", studentService.updateStudent(id, dto));
    }

    @Operation(summary = "删除学生")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ApiResponse.success("删除成功", null);
    }
}
