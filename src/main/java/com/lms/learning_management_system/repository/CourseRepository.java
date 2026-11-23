package com.lms.learning_management_system.repository;

import com.lms.learning_management_system.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
