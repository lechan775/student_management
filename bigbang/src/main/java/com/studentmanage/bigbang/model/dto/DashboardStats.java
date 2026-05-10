package com.studentmanage.bigbang.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private long totalStudents;
    private long totalUsers;
    private Map<String, Long> deptDistribution;
    private Map<String, Long> sexDistribution;
}
