package com.studentmanage.bigbang.service;

import com.studentmanage.bigbang.model.dto.StudentDTO;
import com.studentmanage.bigbang.model.entity.Student;
import com.studentmanage.bigbang.model.mapper.StudentMapper;
import com.studentmanage.bigbang.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepo;
    @Mock private StudentMapper studentMapper;

    @InjectMocks private StudentService studentService;

    @Test
    @DisplayName("分页查询→返回 Page<StudentDTO>")
    void listAllReturnsPage() {
        Student entity = new Student();
        entity.setId(1L);
        entity.setStudentId("2024001");
        entity.setName("测试");
        StudentDTO dto = new StudentDTO();
        dto.setId(1L);
        dto.setStudentId("2024001");
        dto.setName("测试");

        Page<Student> page = new PageImpl<>(List.of(entity));
        when(studentRepo.findAll(any(PageRequest.class))).thenReturn(page);
        when(studentMapper.toDto(any(Student.class))).thenReturn(dto);

        Page<StudentDTO> result = studentService.listAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("2024001", result.getContent().get(0).getStudentId());
    }

    @Test
    @DisplayName("删除学生→成功")
    void deleteStudentSuccess() {
        Student entity = new Student();
        entity.setId(1L);
        entity.setStudentId("2024001");

        when(studentRepo.findById(1L)).thenReturn(Optional.of(entity));
        doNothing().when(studentRepo).deleteById(1L);

        assertDoesNotThrow(() -> studentService.deleteStudent(1L));
        verify(studentRepo).deleteById(1L);
    }

    @Test
    @DisplayName("仪表盘统计→返回正确结构")
    void dashboardReturnsStats() {
        when(studentRepo.count()).thenReturn(5L);
        when(studentRepo.countByDepartment()).thenReturn(List.of(
                new Object[]{"计算机", 3L}, new Object[]{"软件工程", 2L}
        ));
        when(studentRepo.countBySex()).thenReturn(List.of(
                new Object[]{"男", 3L}, new Object[]{"女", 2L}
        ));

        var stats = studentService.getDashboardStats();

        assertEquals(5L, stats.getTotalStudents());
        assertEquals(2, stats.getDeptDistribution().size());
        assertEquals(2, stats.getSexDistribution().size());
    }
}
