package org.sj.capstone.debug.debugbackend.common;

import org.sj.capstone.debug.debugbackend.entity.*;
import org.sj.capstone.debug.debugbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Map;

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

    public static final BoardImage boardImage = BoardImage.builder()
            .originName("test-origin-image.jpg")
            .storedName("test-stored-image.jpg")
            .build();

    public static final Board board = Board.builder()
            .project(project)
            .memo("test-memo")
            .boardImage(boardImage)
            .build();

    public static final Issue issue = Issue.builder()
            .boardImage(boardImage)
            .nameToProbs(Map.of(
                    "질병1", 0.1,
                    "질병2", 0.3,
                    "질병3", 0.5,
                    "질병4", 0.7,
                    "질병5", 0.9,
                    "질병6", 1.0,
                    "질병7", 0.8,
                    "질병8", 0.6,
                    "질병9", 0.4,
                    "질병10", 0.2
            ))
            .build();

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            MemberRepository memberRepository;

            @Autowired
            ProjectRepository projectRepository;

            @Autowired
            BoardRepository boardRepository;

            @Autowired
            BoardImageRepository boardImageRepository;

            @Autowired
            IssueRepository issueRepository;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                memberRepository.save(member);
                projectRepository.save(project);
                boardImageRepository.save(boardImage);
                boardRepository.save(board);
                issueRepository.save(issue);
            }
        };
    }
}
