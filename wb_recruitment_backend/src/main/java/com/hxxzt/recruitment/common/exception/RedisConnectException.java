package com.hxxzt.recruitment.common.exception;

/**
 * Redis 连接异常
 */
public class RedisConnectException extends Exception {

    private Integer code;

    public RedisConnectException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}