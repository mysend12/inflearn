package com.bskim.thymeleaf.Exception;

public class SampleException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String message;

    public String getMessage(){
        return message;
    }

    public SampleException(String message){
        this.message = message;
    }


}
