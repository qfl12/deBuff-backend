package com.debuff.debuffbackend.exception;

/**
 * 业务异常类，用于处理应用程序中的业务逻辑错误
 */
public class BusinessException extends RuntimeException {
    
    /**
     * 构造方法，带有错误消息
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
    }
    
    /**
     * 构造方法，带有错误消息和原因
     * @param message 错误消息
     * @param cause 异常原因
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}