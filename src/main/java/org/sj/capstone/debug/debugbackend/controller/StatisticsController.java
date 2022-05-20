package org.sj.capstone.debug.debugbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sj.capstone.debug.debugbackend.dto.common.ApiResult;
import org.sj.capstone.debug.debugbackend.dto.statistics.StatisticsDto;
import org.sj.capstone.debug.debugbackend.entity.CropType;
import org.sj.capstone.debug.debugbackend.service.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/crop-types")
    public ResponseEntity<ApiResult<StatisticsDto>> getStatisticsByCropType(
            @RequestParam CropType cropType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        ApiResult<StatisticsDto> result = ApiResult.<StatisticsDto>builder()
                .data(statisticsService.getDiseaseWithCropTypes(cropType, startDate, endDate))
                .build();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ApiResult<Integer>> getDiseaseCount(@PathVariable long projectId) {
        ApiResult<Integer> result = ApiResult.<Integer>builder()
                .data(statisticsService.getDiseaseCount(projectId))
                .build();

        return ResponseEntity.ok(result);
    }
}
