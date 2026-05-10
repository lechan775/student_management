package com.studentmanage.universe.controller;

import com.studentmanage.universe.dto.ApiResponse;
import com.studentmanage.universe.dto.DashboardStats;
import com.studentmanage.universe.model.OperationLog;
import com.studentmanage.universe.model.Student;
import com.studentmanage.universe.service.LogService;
import com.studentmanage.universe.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final StudentService studentService;
    private final LogService logService;

    public DashboardController(StudentService studentService, LogService logService) {
        this.studentService = studentService;
        this.logService = logService;
    }

    /** 仪表盘统计数据 */
    @GetMapping("/dashboard")
    public ApiResponse<DashboardStats> dashboard() {
        return ApiResponse.success(studentService.getDashboardStats());
    }

    /** 操作日志（最近20条） */
    @GetMapping("/logs")
    public ApiResponse<List<OperationLog>> logs() {
        return ApiResponse.success(logService.getRecentLogs());
    }

    /** 导出为 Excel */
    @GetMapping("/export/excel")
    public void exportExcel(HttpServletResponse response, Principal principal) throws IOException {
        List<Student> students = studentService.listAll();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String filename = "学生信息_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        response.setHeader("Content-Disposition",
                "attachment; filename*=UTF-8''" + URLEncoder.encode(filename, StandardCharsets.UTF_8));

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("学生信息");

            // 表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 表头行
            String[] headers = {"学号", "姓名", "年龄", "性别", "院系", "班级", "邮箱", "手机"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }

            // 数据行
            int rowIdx = 1;
            for (Student s : students) {
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

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();

            logService.log(principal.getName(), "EXPORT_EXCEL",
                    "导出 " + students.size() + " 条学生数据");
        }
    }
}
