package com.studentmanage.bigbang.repository;

import com.studentmanage.bigbang.model.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学生 Repository
 * 继承 JpaSpecificationExecutor 支持动态查询（姓名 + 院系组合搜索）
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long>,
        JpaSpecificationExecutor<Student> {

    boolean existsByStudentId(String studentId);

    Page<Student> findByNameContaining(String keyword, Pageable pageable);

    Page<Student> findByDepartmentContaining(String dept, Pageable pageable);

    @Query("SELECT s.department, COUNT(s) FROM Student s GROUP BY s.department")
    List<Object[]> countByDepartment();

    @Query("SELECT s.sex, COUNT(s) FROM Student s GROUP BY s.sex")
    List<Object[]> countBySex();
}
