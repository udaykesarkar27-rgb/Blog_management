package com.uday.bolgManagement.exception;

public class UnauthorizedActionException extends  RuntimeException{
    public  UnauthorizedActionException(String message){
        super(message);
    }
}
