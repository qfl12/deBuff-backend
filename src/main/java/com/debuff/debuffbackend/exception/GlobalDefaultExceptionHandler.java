package com.debuff.debuffbackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器，统一处理应用程序中的异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalDefaultExceptionHandler {

    /**
     * 处理业务异常
     * @param e BusinessException实例
     * @return 包含错误信息的响应实体
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理其他未捕获的异常
     * @param e Exception实例
     * @return 包含错误信息的响应实体
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("message", "系统内部错误，请联系管理员");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}