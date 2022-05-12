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
        return projectRepository.findById(projectId)
                .map(ProjectDto::of)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 projectId = " + projectId));
    }

    @Transactional
    public long createProject(ProjectCreationDto creationDto, long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        ">> memberId=" + memberId));
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
        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, ">> projectId=" + projectId));
        Project updatedProject = Project.builder()
                .id(project.getId())
                .name(updateDto.getName())
                .member(project.getMember())
                .startDate(updateDto.getStartDate())
                .endDate(updateDto.getEndDate())
                .cropType(updateDto.getCropType())
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
}
