package com.studentmanage.bigbang.exception;

/**
 * 业务异常 — 全局异常处理器统一捕获并转换为标准 ApiResponse
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    public int getCode() {
        return code;
    }
}
