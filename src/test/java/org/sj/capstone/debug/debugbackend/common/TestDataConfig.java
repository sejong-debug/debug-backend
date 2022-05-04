package org.sj.capstone.debug.debugbackend.common;

import org.sj.capstone.debug.debugbackend.entity.Member;
import org.sj.capstone.debug.debugbackend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestDataConfig {

    public static final Member member = Member.builder()
            .username("user")
            .password("password")
            .name("test-name")
            .build();

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            MemberRepository memberRepository;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                memberRepository.save(member);
            }
        };
    }
}
