package org.sj.capstone.debug.debugbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sj.capstone.debug.debugbackend.dto.common.ApiResult;
import org.sj.capstone.debug.debugbackend.dto.project.ProjectCreationDto;
import org.sj.capstone.debug.debugbackend.dto.project.ProjectDto;
import org.sj.capstone.debug.debugbackend.dto.project.ProjectUpdateDto;
import org.sj.capstone.debug.debugbackend.entity.CropType;
import org.sj.capstone.debug.debugbackend.error.ErrorCode;
import org.sj.capstone.debug.debugbackend.error.exception.BusinessException;
import org.sj.capstone.debug.debugbackend.security.LoginMemberId;
import org.sj.capstone.debug.debugbackend.service.ProjectService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        if (projectService.isProjectNotOwnedByMember(projectId, memberId)) {
            throw new BusinessException(ErrorCode.NOT_OWNED_RESOURCE,
                    ">> projectId=" + projectId + ", memberId=" + memberId);
        }

        ApiResult<ProjectDto> result = ApiResult.<ProjectDto>builder()
                .data(projectService.getProject(projectId))
                .build();

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResult<Long>> updateProject(
            @Valid @RequestBody ProjectUpdateDto updateDto,
            @PathVariable long projectId, @LoginMemberId Long memberId) {
        if (projectService.isProjectNotOwnedByMember(projectId, memberId)) {
            throw new BusinessException(ErrorCode.NOT_OWNED_RESOURCE,
                    ">> projectId=" + projectId + ", memberId=" + memberId);
        }
        ApiResult<Long> result = ApiResult.<Long>builder()
                .data(projectService.updateProject(updateDto, projectId))
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .location(linkTo(methodOn(ProjectController.class).getProject(projectId, memberId)).toUri())
                .body(result);
    }

    @PutMapping("/{projectId}/completed")
    public ResponseEntity<ApiResult<Long>> updateProjectCompleted(
            @PathVariable long projectId, @LoginMemberId Long memberId, @RequestParam boolean completed) {
        if (projectService.isProjectNotOwnedByMember(projectId, memberId)) {
            throw new BusinessException(ErrorCode.NOT_OWNED_RESOURCE,
                    ">> projectId=" + projectId + ", memberId=" + memberId);
        }
        ApiResult<Long> result = ApiResult.<Long>builder()
                .data(projectService.updateProjectCompleted(projectId, completed))
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .location(linkTo(methodOn(ProjectController.class).getProject(projectId, memberId)).toUri())
                .body(result);
    }

    @GetMapping
    public ResponseEntity<ApiResult<Slice<ProjectDto>>> queryProjects(
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "completed", direction = Sort.Direction.ASC),
                    @SortDefault(sort = "endDate", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "startDate", direction = Sort.Direction.DESC)
            }) Pageable pageable,
            @LoginMemberId Long memberId) {

        ApiResult<Slice<ProjectDto>> result = ApiResult.<Slice<ProjectDto>>builder()
                .data(projectService.getProjectSlice(pageable, memberId))
                .build();

        return ResponseEntity.ok(result);
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
