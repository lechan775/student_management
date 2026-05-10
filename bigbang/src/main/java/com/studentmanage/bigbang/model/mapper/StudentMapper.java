package com.studentmanage.bigbang.model.mapper;

import com.studentmanage.bigbang.model.dto.StudentDTO;
import com.studentmanage.bigbang.model.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct: Student Entity ↔ DTO 自动转换
 * componentModel = "spring" → 生成 Spring Bean
 * unmappedTargetPolicy = IGNORE → 未映射字段自动忽略
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {

    StudentDTO toDto(Student entity);

    List<StudentDTO> toDtoList(List<Student> entities);

    /**
     * DTO → Entity: 只映射用户可编辑字段，id/createdAt/updatedAt 由 JPA 自动管理。
     * 使用 source → target 显式映射，其余字段由 IGNORE 策略自动跳过。
     */
    @Mapping(target = "studentId", source = "studentId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "age", source = "age")
    @Mapping(target = "sex", source = "sex")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "className", source = "className")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phone", source = "phone")
    Student toEntity(StudentDTO dto);
}
