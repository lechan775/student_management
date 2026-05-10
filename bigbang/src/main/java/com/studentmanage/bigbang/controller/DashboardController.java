package com.studentmanage.bigbang.controller;

import com.studentmanage.bigbang.model.dto.ApiResponse;
import com.studentmanage.bigbang.model.dto.DashboardStats;
import com.studentmanage.bigbang.model.entity.OperationLog;
import com.studentmanage.bigbang.service.LogService;
import com.studentmanage.bigbang.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Tag(name = "仪表盘 & 导出", description = "数据统计 / Excel 导出 / 操作日志")
@RestController
@RequestMapping("/api")
public class DashboardController {

    private final StudentService studentService;
    private final LogService logService;

    public DashboardController(StudentService studentService, LogService logService) {
        this.studentService = studentService;
        this.logService = logService;
    }

    @Operation(summary = "仪表盘统计数据")
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ApiResponse<DashboardStats> dashboard() {
        return ApiResponse.success(studentService.getDashboardStats());
    }

    @Operation(summary = "导出学生数据为 Excel")
    @GetMapping("/export/excel")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public void exportExcel(HttpServletResponse response) throws IOException {
        // 使用无分页的全量查询
        var allStudents = studentService.listAll(org.springframework.data.domain.Pageable.unpaged());

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String filename = "学生信息_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        response.setHeader("Content-Disposition",
                "attachment; filename*=UTF-8''" + URLEncoder.encode(filename, StandardCharsets.UTF_8));

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("学生信息");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            String[] headers = {"学号", "姓名", "年龄", "性别", "院系", "班级", "邮箱", "手机"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (var s : allStudents.getContent()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(s.getStudentId());
                row.createCell(1).setCellValue(s.getName());
                row.createCell(2).setCellValue(s.getAge());
                row.createCell(3).setCellValue(s.getSex());
                row.createCell(4).setCellValue(s.getDepartment());
                row.createCell(5).setCellValue(s.getClassName());
                row.createCell(6).setCellValue(s.getEmail());
                row.createCell(7).setCellValue(s.getPhone());
            }
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
        }
    }

    @Operation(summary = "操作日志（最近50条，仅ADMIN）")
    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<OperationLog>> logs() {
        return ApiResponse.success(logService.getRecentLogs());
    }
}
