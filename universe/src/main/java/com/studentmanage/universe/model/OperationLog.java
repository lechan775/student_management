package com.studentmanage.universe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 操作日志 — 审计追踪
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operation_logs")
public class OperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String operation;  // LOGIN, ADD_STUDENT, DELETE_STUDENT, UPDATE_STUDENT, EXPORT, etc.

    @Column(columnDefinition = "TEXT")
    private String detail;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public OperationLog(String username, String operation, String detail) {
        this.username = username;
        this.operation = operation;
        this.detail = detail;
        this.createdAt = LocalDateTime.now();
    }
}
