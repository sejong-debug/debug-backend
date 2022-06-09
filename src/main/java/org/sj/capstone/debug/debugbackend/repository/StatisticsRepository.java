package org.sj.capstone.debug.debugbackend.repository;

import org.sj.capstone.debug.debugbackend.dto.statistics.IssueWithProjectDto;
import org.sj.capstone.debug.debugbackend.entity.CropType;
import org.sj.capstone.debug.debugbackend.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Issue, Long> {

    String defaultQuery = "select new org.sj.capstone.debug.debugbackend.dto.statistics.IssueWithProjectDto(p, i)\n" +
            "from Project p\n" +
            "inner join Board b on b.project.id = p.id\n" +
            "inner join BoardImage b_i on b_i.id = b.boardImage.id\n" +
            "inner join Issue i on i.boardImage.id = b_i.id\n";

    @Query(defaultQuery + "where p.cropType = :cropType\n"
    + "and b.createdDate between :startDate and :endDate\n"
    + "order by p.id")
    List<IssueWithProjectDto> findAllForStatisticsByCropType(
            @Param("cropType") CropType cropType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
            );

    @Query(defaultQuery + "where p.id = :projectId")
    List<IssueWithProjectDto> findAllForStatisticsByProjectId(@Param("projectId") Long projectId);

}
