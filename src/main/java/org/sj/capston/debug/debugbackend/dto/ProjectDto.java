package org.sj.capston.debug.debugbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.sj.capston.debug.debugbackend.entity.CropType;
import org.sj.capston.debug.debugbackend.entity.Project;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class ProjectDto {

    private long projectId;

    private String name;

    private CropType cropType;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean completed;

    public static ProjectDto of(Project project) {
        return ProjectDto.builder()
                .projectId(project.getId())
                .name(project.getName())
                .cropType(project.getCropType())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .completed(project.isCompleted())
                .build();
    }
}
