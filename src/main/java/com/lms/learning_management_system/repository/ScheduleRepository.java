package com.lms.learning_management_system.repository;

import com.lms.learning_management_system.domain.entity.Group;
import com.lms.learning_management_system.domain.entity.Schedule;
import com.lms.learning_management_system.domain.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Page<Schedule> findByGroup(Group group, Pageable pageable);

    Page<Schedule> findByTeacher(Teacher teacher, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Schedule s WHERE s.endDate < :date")
    int deleteOlderThan(@Param("date") LocalDateTime date);
}
