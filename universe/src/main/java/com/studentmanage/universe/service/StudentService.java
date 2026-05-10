package com.studentmanage.universe.service;

import com.studentmanage.universe.dto.DashboardStats;
import com.studentmanage.universe.model.Student;
import com.studentmanage.universe.repository.StudentRepository;
import com.studentmanage.universe.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    private final StudentRepository studentRepo;
    private final UserRepository userRepo;
    private final LogService logService;

    public StudentService(StudentRepository studentRepo, UserRepository userRepo,
                          LogService logService) {
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.logService = logService;
    }

    // ==================== CRUD ====================

    public Student addStudent(Student s, String operator) {
        if (studentRepo.existsByStudentId(s.getStudentId())) {
            throw new RuntimeException("学号 " + s.getStudentId() + " 已存在");
        }
        Student saved = studentRepo.save(s);
        logService.log(operator, "ADD_STUDENT", "添加学生: " + s.getStudentId() + " " + s.getName());
        return saved;
    }

    public void deleteStudent(Long id, String operator) {
        Student s = studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("学生不存在"));
        studentRepo.deleteById(id);
        logService.log(operator, "DELETE_STUDENT", "删除学生: " + s.getStudentId() + " " + s.getName());
    }

    public Student updateStudent(Long id, Student updated, String operator) {
        Student s = studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("学生不存在"));
        s.setName(updated.getName());
        s.setAge(updated.getAge());
        s.setSex(updated.getSex());
        s.setDepartment(updated.getDepartment());
        s.setClassName(updated.getClassName());
        s.setEmail(updated.getEmail());
        s.setPhone(updated.getPhone());
        Student saved = studentRepo.save(s);
        logService.log(operator, "UPDATE_STUDENT", "更新学生: " + s.getStudentId());
        return saved;
    }

    public List<Student> listAll() {
        return studentRepo.findAll();
    }

    public List<Student> searchByName(String keyword) {
        return studentRepo.findByNameContaining(keyword);
    }

    public List<Student> searchByDepartment(String dept) {
        return studentRepo.findByDepartmentContaining(dept);
    }

    // ==================== 仪表盘统计 ====================

    public DashboardStats getDashboardStats() {
        long totalStudents = studentRepo.count();
        long totalUsers = userRepo.count();

        Map<String, Long> deptDist = new LinkedHashMap<>();
        for (Object[] row : studentRepo.countByDepartment()) {
            String dept = (String) row[0];
            if (dept == null || dept.isEmpty()) dept = "未分配";
            deptDist.put(dept, (Long) row[1]);
        }

        Map<String, Long> sexDist = new LinkedHashMap<>();
        for (Object[] row : studentRepo.countBySex()) {
            sexDist.put((String) row[0], (Long) row[1]);
        }

        return new DashboardStats(totalStudents, deptDist, sexDist, totalUsers);
    }
}
