package com.studentmanage.universe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * 仪表盘统计数据
 */
@Data
@AllArgsConstructor
public class DashboardStats {
    private long totalStudents;              // 学生总数
    private Map<String, Long> deptDistribution;  // 院系分布
    private Map<String, Long> sexDistribution;   // 性别分布
    private long totalUsers;                  // 用户总数
}
