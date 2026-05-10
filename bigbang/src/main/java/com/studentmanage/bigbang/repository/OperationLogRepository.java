package com.studentmanage.bigbang.repository;

import com.studentmanage.bigbang.model.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
    List<OperationLog> findTop50ByOrderByCreatedAtDesc();
}
