package com.lms.learning_management_system.repository;

import com.lms.learning_management_system.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
