package com.studentmanage.bigbang.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenRefreshRequest {
    @NotBlank(message = "refreshToken 不能为空")
    private String refreshToken;
}
