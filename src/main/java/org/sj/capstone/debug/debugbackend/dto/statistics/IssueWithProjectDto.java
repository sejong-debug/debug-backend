package org.sj.capstone.debug.debugbackend.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sj.capstone.debug.debugbackend.entity.Issue;
import org.sj.capstone.debug.debugbackend.entity.Project;

@Data
@AllArgsConstructor
public class IssueWithProjectDto {
    private Project project;

    private Issue issue;
}
