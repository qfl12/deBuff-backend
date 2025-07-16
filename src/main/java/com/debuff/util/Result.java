package com.debuff.util;

import lombok.Data;

/**
 * 统一API响应结果封装类
 * 包含响应码、消息和数据三部分
 */
@Data
public class Result<T> {
    private int code; // 响应码：0-成功，非0-失败
    private String message; // 响应消息
    private T data; // 响应数据

    /**
     * 成功响应构造方法
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 封装后的成功响应对象
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 失败响应构造方法
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 封装后的失败响应对象
     */
    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}