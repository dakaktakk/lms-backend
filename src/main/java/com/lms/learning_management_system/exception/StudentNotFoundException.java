package com.lms.learning_management_system.exception;

public class StudentNotFoundException extends ResourceNotFoundException {

    public StudentNotFoundException(Long id) {
        super("Студент с ID " + id + " не найден");
    }
}
