package com.studentmanage.bigbang.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Size(min = 3, max = 15)
    private String username;

    @NotBlank @Size(min = 6, max = 50)
    private String password;

    @NotBlank
    private String personId;

    @NotBlank
    private String phoneNumber;
}
