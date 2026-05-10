package com.studentmanage.bigbang.service;

import com.studentmanage.bigbang.exception.BusinessException;
import com.studentmanage.bigbang.model.dto.DashboardStats;
import com.studentmanage.bigbang.model.dto.StudentDTO;
import com.studentmanage.bigbang.model.entity.Student;
import com.studentmanage.bigbang.model.mapper.StudentMapper;
import com.studentmanage.bigbang.repository.StudentRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StudentService {

    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepo, StudentMapper studentMapper) {
        this.studentRepo = studentRepo;
        this.studentMapper = studentMapper;
    }

    // ==================== 增 ====================

    @Transactional
    @CacheEvict(value = {"students", "dashboard"}, allEntries = true)
    public StudentDTO addStudent(StudentDTO dto) {
        if (studentRepo.existsByStudentId(dto.getStudentId())) {
            throw new BusinessException("学号 " + dto.getStudentId() + " 已存在");
        }
        Student entity = studentMapper.toEntity(dto);
        Student saved = studentRepo.save(entity);
        log.info("添加学生: {} {}", saved.getStudentId(), saved.getName());
        return studentMapper.toDto(saved);
    }

    // ==================== 删 ====================

    @Transactional
    @CacheEvict(value = {"students", "dashboard"}, allEntries = true)
    public void deleteStudent(Long id) {
        Student s = studentRepo.findById(id)
                .orElseThrow(() -> new BusinessException("学生不存在"));
        studentRepo.deleteById(id);
        log.info("删除学生: {} {}", s.getStudentId(), s.getName());
    }

    // ==================== 改 ====================

    @Transactional
    @CacheEvict(value = {"students", "dashboard"}, allEntries = true)
    public StudentDTO updateStudent(Long id, StudentDTO dto) {
        Student s = studentRepo.findById(id)
                .orElseThrow(() -> new BusinessException("学生不存在"));
        s.setName(dto.getName());
        s.setAge(dto.getAge());
        s.setSex(dto.getSex());
        s.setDepartment(dto.getDepartment());
        s.setClassName(dto.getClassName());
        s.setEmail(dto.getEmail());
        s.setPhone(dto.getPhone());
        Student saved = studentRepo.save(s);
        log.info("更新学生: {}", s.getStudentId());
        return studentMapper.toDto(saved);
    }

    // ==================== 查（后端分页 + Redis缓存） ====================

    @Cacheable(value = "students", key = "#pageable.pageNumber + '_' + #pageable.pageSize", unless = "#result.totalElements == 0")
    public Page<StudentDTO> listAll(Pageable pageable) {
        return studentRepo.findAll(pageable).map(studentMapper::toDto);
    }

    @Cacheable(value = "students", key = "'search_' + #keyword + '_' + #pageable.pageNumber")
    public Page<StudentDTO> searchByName(String keyword, Pageable pageable) {
        return studentRepo.findByNameContaining(keyword, pageable).map(studentMapper::toDto);
    }

    public Page<StudentDTO> searchByDepartment(String dept, Pageable pageable) {
        return studentRepo.findByDepartmentContaining(dept, pageable).map(studentMapper::toDto);
    }

    /** Specification 动态组合查询（姓名 + 院系 联合筛选） */
    public Page<StudentDTO> search(String keyword, String dept, Pageable pageable) {
        Specification<Student> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + keyword + "%"));
            }
            if (dept != null && !dept.isEmpty()) {
                predicates.add(cb.like(root.get("department"), "%" + dept + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return studentRepo.findAll(spec, pageable).map(studentMapper::toDto);
    }

    // ==================== 仪表盘 ====================

    @Cacheable(value = "dashboard", key = "'stats'")
    public DashboardStats getDashboardStats() {
        long totalStudents = studentRepo.count();

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

        return new DashboardStats(totalStudents, 0, deptDist, sexDist);
    }

    public long count() {
        return studentRepo.count();
    }
}
