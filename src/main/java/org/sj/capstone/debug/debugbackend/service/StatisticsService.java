package org.sj.capstone.debug.debugbackend.service;

import lombok.RequiredArgsConstructor;
import org.sj.capstone.debug.debugbackend.dto.statistics.IssueWithProjectDto;
import org.sj.capstone.debug.debugbackend.dto.statistics.StatisticsDto;
import org.sj.capstone.debug.debugbackend.entity.CropType;
import org.sj.capstone.debug.debugbackend.repository.StatisticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public StatisticsDto getDiseaseWithCropTypes(CropType cropType, LocalDate startDate, LocalDate endDate) {
        startDate = (startDate != null) ? startDate : LocalDate.MIN;
        endDate = (endDate != null) ? endDate : LocalDate.MAX;
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);

        List<IssueWithProjectDto> data = statisticsRepository.findAllForStatisticsByCropType(cropType, startDateTime, endDateTime);

        StatisticsDto result = new StatisticsDto();
        result.setCropType(cropType);

        int projectCount = 0;
        long prevProjectId = 0;
        Map<String, Integer> diseases = new HashMap<>();
        for (IssueWithProjectDto row: data) {
            long projectId = row.getProject().getId();
            if (projectId != prevProjectId) {
                prevProjectId = projectId;
                projectCount++;
            }

            Map<String, Double> issues = row.getIssue().getNameToProbs();
            for (Map.Entry<String, Double> entry : issues.entrySet()) {
                if (entry.getValue() >= 0.5) {
                    diseases.put(entry.getKey(), diseases.getOrDefault(entry.getKey(), 0) + 1);
                }
            }
        }

        result.setProjectCount(projectCount);
        result.setDiseases(diseases);

        return result;
    }

    public int getDiseaseCount(Long projectId) {
        List<IssueWithProjectDto> data = statisticsRepository.findAllForStatisticsByProjectId(projectId);
        int count = 0;
        for (IssueWithProjectDto row: data){
            Map<String, Double> issues = row.getIssue().getNameToProbs();
            for (String key: issues.keySet()) {
                if (issues.get(key) >= 0.5)
                    count++;
            }
        }

        return count;
    }

}
