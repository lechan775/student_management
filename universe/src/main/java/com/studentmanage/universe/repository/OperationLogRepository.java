package com.studentmanage.universe.repository;

import com.studentmanage.universe.model.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {

    List<OperationLog> findTop20ByOrderByCreatedAtDesc();
}
