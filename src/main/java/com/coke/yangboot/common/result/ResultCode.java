package com.coke.yangboot.common.result;

/**
 * 返回码定义
 * 规定:
 * #200表示成功
 * #500失败
 * #后面对什么的操作自己在这里注明就行了
 */
public enum ResultCode {
    /* 成功 */
    SUCCESS(200, "成功"),

    /* 默认失败 */
    FAIL(500, "失败");


    private Integer code;

    private String message;

    ResultCode(Integer code,String message){
        this.code = code;
        this.message = message;
    }


    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}