package org.sj.capston.debug.debugbackend.repository;

import org.sj.capston.debug.debugbackend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
