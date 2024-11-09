package com.demo_emp_service.exceptions;

public class SkillAlreadyExistsException extends RuntimeException{
    public  SkillAlreadyExistsException(String message) {
         super(message);
    }
}
