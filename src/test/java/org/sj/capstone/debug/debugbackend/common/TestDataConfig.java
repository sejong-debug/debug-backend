package org.sj.capstone.debug.debugbackend.common;

import org.sj.capstone.debug.debugbackend.entity.CropType;
import org.sj.capstone.debug.debugbackend.entity.Member;
import org.sj.capstone.debug.debugbackend.entity.Project;
import org.sj.capstone.debug.debugbackend.repository.MemberRepository;
import org.sj.capstone.debug.debugbackend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@TestConfiguration
public class TestDataConfig {

    public static final Member member = Member.builder()
            .username("user")
            .password("password")
            .name("test-name")
            .build();

    public static final Project project = Project.builder()
            .member(member)
            .name("테스트 프로젝트")
            .cropType(CropType.가지)
            .startDate(LocalDate.of(2022, 3, 1))
            .endDate(LocalDate.of(2022, 12, 25))
            .build();

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            MemberRepository memberRepository;

            @Autowired
            ProjectRepository projectRepository;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                memberRepository.save(member);
                projectRepository.save(project);
            }
        };
    }
}
