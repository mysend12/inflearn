package com.bskim.thymeleaf.Exception;

public class ExceptionModel {

    private String message;
    private Integer code;

    public ExceptionModel(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ExceptionModel [code=" + code + ", message=" + message + "]";
    }
}