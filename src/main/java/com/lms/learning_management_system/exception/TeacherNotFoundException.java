package com.lms.learning_management_system.exception;

public class TeacherNotFoundException extends ResourceNotFoundException {

    public TeacherNotFoundException(Long id) {
        super("Преподаватель с ID " + id + " не найден");
    }
}
