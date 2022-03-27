package org.sj.capston.debug.debugbackend.repository;

import org.sj.capston.debug.debugbackend.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {
}
