package top.summus.sword.exception;

import lombok.Getter;

public class WrongStatusCodeException extends RuntimeException{
    @Getter
    private int code;
    public WrongStatusCodeException(String message){
        super(message);
    }

    public WrongStatusCodeException(int code){
        this.code=code;
    }


    @Override
    public String getMessage() {
       return "error status code "+code;
    }

    public WrongStatusCodeException(){}
}
