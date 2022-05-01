package org.sj.capstone.debug.debugbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sj.capstone.debug.debugbackend.dto.ProjectCreationDto;
import org.sj.capstone.debug.debugbackend.dto.ProjectDto;
import org.sj.capstone.debug.debugbackend.security.MemberContext;
import org.sj.capstone.debug.debugbackend.service.ProjectService;
import org.sj.capstone.debug.debugbackend.entity.CropType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable long projectId){
        return ResponseEntity.ok(projectService.getProject(projectId));
    }

    @GetMapping
    public ResponseEntity<Slice<ProjectDto>> queryProjects(
            Pageable pageable, @AuthenticationPrincipal MemberContext memberContext) {
        return ResponseEntity
                .ok(projectService.getProjectSlice(pageable, memberContext.getMemberId()));
    }

    @PostMapping
    public ResponseEntity<Void> createProject(
            @Validated @RequestBody ProjectCreationDto creationDto,
            @AuthenticationPrincipal MemberContext memberContext) {

        long memberId = memberContext.getMemberId();
        long projectId = projectService.createProject(creationDto, memberId);
        return ResponseEntity
                .created(linkTo(ProjectController.class).slash(projectId).toUri())
                .build();
    }


    @GetMapping("/crop-types")
    public ResponseEntity<List<String>> getAllCropTypes() {
        List<String> result = Arrays.stream(CropType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
