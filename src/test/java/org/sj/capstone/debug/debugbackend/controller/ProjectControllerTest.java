package org.sj.capstone.debug.debugbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.sj.capstone.debug.debugbackend.common.RestDocsConfig;
import org.sj.capstone.debug.debugbackend.dto.member.MemberJoinDto;
import org.sj.capstone.debug.debugbackend.dto.ProjectCreationDto;
import org.sj.capstone.debug.debugbackend.security.JwtTokenProvider;
import org.sj.capstone.debug.debugbackend.dto.security.JwtResponseDto;
import org.sj.capstone.debug.debugbackend.dto.security.LoginDto;
import org.sj.capstone.debug.debugbackend.entity.CropType;
import org.sj.capstone.debug.debugbackend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfig.class)
@ActiveProfiles("test")
@Transactional
class ProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberService memberService;

    @Test
    void getCropTypesTest() throws Exception{
        String loginToken = JwtTokenProvider.TOKEN_PREFIX + joinAndLogin();
        mockMvc.perform(get("/projects/crop-types")
                        .header(HttpHeaders.AUTHORIZATION, loginToken))
                .andDo(print());
    }

    @Test
    void createProjectTest() throws Exception {
        String loginToken = JwtTokenProvider.TOKEN_PREFIX + joinAndLogin();

        ProjectCreationDto creationDto = new ProjectCreationDto();
        creationDto.setName("test-project");
        creationDto.setStartDate(LocalDate.of(2022, 3, 1));
        creationDto.setEndDate(LocalDate.of(2022, 12, 1));
        creationDto.setCropType(CropType.REDBEAN);

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .header(HttpHeaders.AUTHORIZATION, loginToken)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andDo(print());
    }


    private String joinAndLogin() throws Exception {
        String username = "test-username";
        String password = "test-password!";
        String name = "test-name";

        MemberJoinDto memberJoinDto = new MemberJoinDto();
        memberJoinDto.setUsername(username);
        memberJoinDto.setPassword(password);
        memberJoinDto.setName(name);
        memberService.join(memberJoinDto);

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(username);
        loginDto.setPassword(password);

        ResultActions perform = mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(loginDto)));
        return objectMapper.readValue(perform
                .andReturn()
                .getResponse()
                .getContentAsString(),
                JwtResponseDto.class).getAccessToken();
    }
}