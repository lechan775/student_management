package com.studentmanage.universe.repository;

import com.studentmanage.universe.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByStudentId(String studentId);

    List<Student> findByNameContaining(String keyword);

    List<Student> findByDepartmentContaining(String dept);

    @Query("SELECT s.department, COUNT(s) FROM Student s GROUP BY s.department")
    List<Object[]> countByDepartment();

    @Query("SELECT s.sex, COUNT(s) FROM Student s GROUP BY s.sex")
    List<Object[]> countBySex();
}
