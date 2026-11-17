package com.lms.learning_management_system.exception;

public class CourseNotFoundException extends ResourceNotFoundException {

    public CourseNotFoundException(Long id) {
        super("Курс с ID " + id + " не найден");
    }

    public CourseNotFoundException(String message) {
        super(message);
    }
}
