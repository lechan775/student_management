package com.studentmanage.universe.service;

import com.studentmanage.universe.model.OperationLog;
import com.studentmanage.universe.repository.OperationLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    private final OperationLogRepository logRepo;

    public LogService(OperationLogRepository logRepo) {
        this.logRepo = logRepo;
    }

    public void log(String username, String operation, String detail) {
        logRepo.save(new OperationLog(username, operation, detail));
    }

    public List<OperationLog> getRecentLogs() {
        return logRepo.findTop20ByOrderByCreatedAtDesc();
    }
}
