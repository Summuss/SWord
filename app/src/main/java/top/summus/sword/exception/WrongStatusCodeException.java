package top.summus.sword.exception;

public class WrongStatusCodeException extends RuntimeException{
    public WrongStatusCodeException(String message){
        super(message);
    }

    public WrongStatusCodeException(){}
}
