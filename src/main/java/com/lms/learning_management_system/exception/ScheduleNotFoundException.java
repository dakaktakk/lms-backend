package com.lms.learning_management_system.exception;

public class ScheduleNotFoundException extends ResourceNotFoundException {

    public ScheduleNotFoundException(Long id) {
        super("Расписание с ID " + id + " не найдено");
    }
}
