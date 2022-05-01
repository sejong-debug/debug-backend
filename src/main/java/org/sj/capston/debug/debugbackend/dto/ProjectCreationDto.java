package org.sj.capston.debug.debugbackend.dto;

import lombok.Data;
import org.sj.capston.debug.debugbackend.entity.CropType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ProjectCreationDto {

    @NotEmpty
    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private CropType cropType;
}
