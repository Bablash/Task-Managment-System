package com.example.demo.exception;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException () {
        super("Task editing is available only to the author");
    }
}
