package org.sj.capstone.debug.debugbackend.dto.issue;

import lombok.Data;

import java.util.List;

@Data
public class IssueDetectionDto {

    private Long boardImageId;

    private List<Double> issueProbs;
}
