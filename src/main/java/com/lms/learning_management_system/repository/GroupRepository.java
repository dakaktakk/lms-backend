package com.lms.learning_management_system.repository;

import com.lms.learning_management_system.domain.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}