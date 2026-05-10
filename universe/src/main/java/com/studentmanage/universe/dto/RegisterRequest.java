package com.studentmanage.universe.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String personId;
    private String phoneNumber;
}
