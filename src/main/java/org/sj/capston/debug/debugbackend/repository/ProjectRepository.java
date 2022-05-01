package org.sj.capston.debug.debugbackend.repository;

import org.sj.capston.debug.debugbackend.entity.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Slice<Project> findAllByMemberId(Pageable pageable, long memberId);
}
