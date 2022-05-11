package org.sj.capstone.debug.debugbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sj.capstone.debug.debugbackend.dto.common.ApiResult;
import org.sj.capstone.debug.debugbackend.dto.project.ProjectCreationDto;
import org.sj.capstone.debug.debugbackend.dto.project.ProjectDto;
import org.sj.capstone.debug.debugbackend.entity.CropType;
import org.sj.capstone.debug.debugbackend.error.ErrorCode;
import org.sj.capstone.debug.debugbackend.error.exception.BusinessException;
import org.sj.capstone.debug.debugbackend.security.LoginMemberId;
import org.sj.capstone.debug.debugbackend.security.MemberContext;
import org.sj.capstone.debug.debugbackend.service.ProjectService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResult<ProjectDto>> getProject(
            @PathVariable long projectId, @LoginMemberId Long memberId) {
        if (!projectService.isProjectOwnedByMember(projectId, memberId)) {
            throw new BusinessException(ErrorCode.NOT_OWNED_RESOURCE,
                    ">> projectId=" + projectId + ", memberId=" + memberId);
        }

        ApiResult<ProjectDto> result = ApiResult.<ProjectDto>builder()
                .data(projectService.getProject(projectId))
                .build();

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<Slice<ProjectDto>> queryProjects(
            Pageable pageable, @AuthenticationPrincipal MemberContext memberContext) {
        return ResponseEntity
                .ok(projectService.getProjectSlice(pageable, memberContext.getMemberId()));
    }

    @PostMapping
    public ResponseEntity<ApiResult<Long>> createProject(
            @Valid @RequestBody ProjectCreationDto creationDto,
            @LoginMemberId Long memberId) {

        long projectId = projectService.createProject(creationDto, memberId);
        ApiResult<Long> result = ApiResult.<Long>builder()
                .data(projectId)
                .build();

        return ResponseEntity
                .created(linkTo(methodOn(ProjectController.class).getProject(projectId, memberId)).toUri())
                .body(result);
    }


    @GetMapping("/crop-types")
    public ResponseEntity<ApiResult<List<String>>> getAllCropTypes() {
        List<String> cropTypes = Arrays.stream(CropType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        ApiResult<List<String>> result = ApiResult.<List<String>>builder()
                .data(cropTypes)
                .build();
        return ResponseEntity.ok(result);
    }
}
