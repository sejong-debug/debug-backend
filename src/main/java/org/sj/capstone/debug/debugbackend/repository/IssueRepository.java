package org.sj.capstone.debug.debugbackend.repository;

import org.sj.capstone.debug.debugbackend.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findAllByBoardImageId(long boardImageId);

    boolean existsByBoardImageId(long boardImageId);
}
