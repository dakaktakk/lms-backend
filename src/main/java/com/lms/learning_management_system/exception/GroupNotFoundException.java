package com.lms.learning_management_system.exception;

public class GroupNotFoundException extends ResourceNotFoundException {

    public GroupNotFoundException(Long id) {
        super("Группа с ID " + id + " не найдена");
    }
}
