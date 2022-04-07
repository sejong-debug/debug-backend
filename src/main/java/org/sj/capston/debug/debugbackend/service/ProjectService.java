package org.sj.capston.debug.debugbackend.service;

import lombok.RequiredArgsConstructor;
import org.sj.capston.debug.debugbackend.dto.ProjectCreationDto;
import org.sj.capston.debug.debugbackend.dto.ProjectDto;
import org.sj.capston.debug.debugbackend.entity.Member;
import org.sj.capston.debug.debugbackend.entity.Project;
import org.sj.capston.debug.debugbackend.repository.MemberRepository;
import org.sj.capston.debug.debugbackend.repository.ProjectRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final MemberRepository memberRepository;

    public long createProject(ProjectCreationDto creationDto, long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 memberId = " + memberId));
        Project project = Project.builder()
                .name(creationDto.getName())
                .member(member)
                .startDate(creationDto.getStartDate())
                .endDate(creationDto.getEndDate())
                .build();
        return projectRepository.save(project).getId();
    }

    public Slice<ProjectDto> getProjectSlice(Pageable pageable, long memberId) {
        return projectRepository.findAllByMemberId(pageable, memberId)
                .map(ProjectDto::of);
    }
}
