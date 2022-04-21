package org.sj.capston.debug.debugbackend.repository;

import org.junit.jupiter.api.Test;
import org.sj.capston.debug.debugbackend.dto.ProjectDto;
import org.sj.capston.debug.debugbackend.entity.Project;
import org.sj.capston.debug.debugbackend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Test
    void findAllByMemberId() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        Slice<Project> projects = projectRepository.findAllByMemberId(pageRequest, 1L);
        System.out.println(projects);
    }
}