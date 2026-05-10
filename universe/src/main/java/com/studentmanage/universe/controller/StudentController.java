package com.studentmanage.universe.controller;

import com.studentmanage.universe.dto.ApiResponse;
import com.studentmanage.universe.model.Student;
import com.studentmanage.universe.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /** 全量列表 */
    @GetMapping
    public ApiResponse<List<Student>> listAll() {
        return ApiResponse.success(studentService.listAll());
    }

    /** 添加学生 */
    @PostMapping
    public ApiResponse<Student> add(@RequestBody Student student, Principal principal) {
        try {
            return ApiResponse.success("添加成功", studentService.addStudent(student, principal.getName()));
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /** 删除学生 */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id, Principal principal) {
        try {
            studentService.deleteStudent(id, principal.getName());
            return ApiResponse.success("删除成功", null);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /** 更新学生 */
    @PutMapping("/{id}")
    public ApiResponse<Student> update(@PathVariable Long id, @RequestBody Student student,
                                       Principal principal) {
        try {
            return ApiResponse.success("更新成功", studentService.updateStudent(id, student, principal.getName()));
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /** 按姓名模糊搜索 */
    @GetMapping("/search")
    public ApiResponse<List<Student>> searchByName(@RequestParam String name) {
        return ApiResponse.success(studentService.searchByName(name));
    }

    /** 按院系过滤 */
    @GetMapping("/filter")
    public ApiResponse<List<Student>> filterByDepartment(@RequestParam String dept) {
        return ApiResponse.success(studentService.searchByDepartment(dept));
    }
}
