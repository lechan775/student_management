package com.studentmanage.bigbang.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 学生 DTO — 用于 API 输入/输出，与 Entity 解耦
 */
@Data
public class StudentDTO {
    private Long id;

    @NotBlank(message = "学号不能为空")
    private String studentId;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @Min(value = 1, message = "年龄必须大于0")
    @Max(value = 150, message = "年龄不能超过150")
    private Integer age;

    @NotBlank(message = "性别不能为空")
    private String sex;

    private String department;
    private String className;
    private String email;
    private String phone;
    private String avatarUrl;
    private String createdAt;
    private String updatedAt;
}
