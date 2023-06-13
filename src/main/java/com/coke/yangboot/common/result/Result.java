package com.coke.yangboot.common.result;

import lombok.Data;

/**
 * 接口返回工具类
 */
@Data
public class Result<T> {
    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    /**
     * 构造方法
     * @param code
     * @param data
     * @param message
     */
    private Result(int code,T data, String message){
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Result(int code, String message) {
        this(code,null,message);
    }

    /**
     * 调用成功
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(){
        return new Result(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMessage());
    }

    public static <T> Result<T> success(String mesage){
        return new Result(ResultCode.SUCCESS.getCode(),mesage);
    }
    public static <T> Result<T> success(String mesage,T data){
        return new Result(ResultCode.SUCCESS.getCode(),data,mesage);
    }

    public static <T> Result<T> success(T data){
        return new Result(ResultCode.SUCCESS.getCode(),data,ResultCode.SUCCESS.getMessage());
    }

    /**
     * 调用失败
     * @return
     * @param <T>
     */
    public static <T> Result<T> fail(){
        return new Result(ResultCode.FAIL.getCode(),null,ResultCode.FAIL.getMessage());
    }
    public static <T> Result<T> fail(String message){
        return new Result(ResultCode.FAIL.getCode(),null,message);
    }

}