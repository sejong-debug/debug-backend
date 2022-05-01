package org.sj.capstone.debug.debugbackend.repository;

import org.junit.jupiter.api.Test;
import org.sj.capstone.debug.debugbackend.entity.Project;
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