package org.sj.capstone.debug.debugbackend.service;

import lombok.RequiredArgsConstructor;
import org.sj.capstone.debug.debugbackend.dto.project.ProjectCreationDto;
import org.sj.capstone.debug.debugbackend.dto.project.ProjectDto;
import org.sj.capstone.debug.debugbackend.dto.project.ProjectUpdateDto;
import org.sj.capstone.debug.debugbackend.entity.Member;
import org.sj.capstone.debug.debugbackend.entity.Project;
import org.sj.capstone.debug.debugbackend.error.ErrorCode;
import org.sj.capstone.debug.debugbackend.error.exception.BusinessException;
import org.sj.capstone.debug.debugbackend.repository.MemberRepository;
import org.sj.capstone.debug.debugbackend.repository.ProjectRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final MemberRepository memberRepository;

    public ProjectDto getProject(long projectId) {
        return ProjectDto.of(getProjectEntity(projectId));
    }

    @Transactional
    public long createProject(ProjectCreationDto creationDto, long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, ">> memberId=" + memberId));
        Project project = Project.builder()
                .name(creationDto.getName())
                .member(member)
                .startDate(creationDto.getStartDate())
                .endDate(creationDto.getEndDate())
                .cropType(creationDto.getCropType())
                .build();
        return projectRepository.save(project).getId();
    }

    @Transactional
    public long updateProject(ProjectUpdateDto updateDto, long projectId) {
        Project project = getProjectEntity(projectId);
        Project updatedProject = Project.builder()
                .id(project.getId())
                .member(project.getMember())
                .name(updateDto.getName())
                .startDate(updateDto.getStartDate())
                .endDate(updateDto.getEndDate())
                .cropType(updateDto.getCropType())
                .completed(updateDto.getCompleted())
                .build();
        return projectRepository.save(updatedProject).getId();
    }

    public Slice<ProjectDto> getProjectSlice(Pageable pageable, long memberId) {
        return projectRepository.findAllByMemberId(pageable, memberId)
                .map(ProjectDto::of);
    }

    public boolean isProjectNotOwnedByMember(long projectId, long memberId) {
        return !projectRepository.existsByIdAndMemberId(projectId, memberId);
    }

    @Transactional
    public long updateProjectCompleted(long projectId, boolean completed) {
        Project project = getProjectEntity(projectId);
        Project updatedProject = Project.builder()
                .id(project.getId())
                .member(project.getMember())
                .name(project.getName())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .cropType(project.getCropType())
                .completed(completed)
                .build();
        return projectRepository.save(updatedProject).getId();
    }

    private Project getProjectEntity(long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() ->
                new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, ">> projectId=" + projectId));
    }
}
