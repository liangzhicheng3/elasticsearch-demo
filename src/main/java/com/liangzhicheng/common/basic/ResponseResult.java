package com.liangzhicheng.common.basic;

import lombok.Data;

@Data
public class ResponseResult<T> {

    private Integer code;
    private String message;
    private T data;

    protected ResponseResult() {

    }

    protected ResponseResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseResult<T> success() {
        return success(null);
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> ResponseResult<T> success(T data, String message) {
        return new ResponseResult<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    public static <T> ResponseResult<T> failed(IErrorCode errorCode) {
        return new ResponseResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> ResponseResult<T> failed(String message) {
        return new ResponseResult<T>(ResultCode.FAILED.getCode(), message, null);
    }

    public static <T> ResponseResult<T> failed() {
        return failed(ResultCode.FAILED);
    }

    public static <T> ResponseResult<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }

    public static <T> ResponseResult<T> validateFailed(String message) {
        return new ResponseResult<T>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
    }

    public static <T> ResponseResult<T> unauthorized(T data) {
        return new ResponseResult<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    public static <T> ResponseResult<T> forbidden(T data) {
        return new ResponseResult<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }

}
