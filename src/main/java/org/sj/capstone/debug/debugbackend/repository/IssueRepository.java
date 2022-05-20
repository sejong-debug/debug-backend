package org.sj.capstone.debug.debugbackend.repository;

import org.sj.capstone.debug.debugbackend.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("select i from Issue i" +
            " inner join BoardImage bi on bi.id = i.boardImage.id" +
            " inner join Board b on b.boardImage.id = bi.id" +
            " where bi.id = :boardId")
    Optional<Issue> findByBoardId(@Param("boardId") long boardId);
}
