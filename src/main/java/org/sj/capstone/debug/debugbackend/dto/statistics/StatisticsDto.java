package org.sj.capstone.debug.debugbackend.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sj.capstone.debug.debugbackend.entity.CropType;

import java.util.Map;

@Data
public class StatisticsDto {
    private CropType cropType;

    private int projectCount;

    private Map<String, Integer> diseases;

}
