package com.studentmanage.bigbang.service;

import com.studentmanage.bigbang.model.entity.OperationLog;
import com.studentmanage.bigbang.repository.OperationLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    private final OperationLogRepository logRepo;

    public LogService(OperationLogRepository logRepo) {
        this.logRepo = logRepo;
    }

    public List<OperationLog> getRecentLogs() {
        return logRepo.findTop50ByOrderByCreatedAtDesc();
    }
}
