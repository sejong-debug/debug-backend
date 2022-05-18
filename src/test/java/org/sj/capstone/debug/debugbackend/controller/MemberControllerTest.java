package org.sj.capstone.debug.debugbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sj.capstone.debug.debugbackend.common.RestDocsConfig;
import org.sj.capstone.debug.debugbackend.common.TestDataConfig;
import org.sj.capstone.debug.debugbackend.dto.member.MemberJoinDto;
import org.sj.capstone.debug.debugbackend.entity.Member;
import org.sj.capstone.debug.debugbackend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import({RestDocsConfig.class, TestDataConfig.class})
@ActiveProfiles("test")
@Transactional
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 회원가입_성공() throws Exception {
        String username = "join-username";
        String password = "join-password!";
        String name = "join-name";

        MemberJoinDto memberJoinDto = new MemberJoinDto();
        memberJoinDto.setUsername(username);
        memberJoinDto.setPassword(password);
        memberJoinDto.setName(name);

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("success").value(true))
                .andDo(document("join",
                        requestFields(
                                fieldWithPath("username").description("회원가입 ID"),
                                fieldWithPath("password").description("회원가입 비밀번호"),
                                fieldWithPath("name").description("회원가입 이름")
                        ),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부")
                        )
                ));
    }

    @Test
    void 아이디_중복체크() throws Exception {
        String username = "unique-username";

        mockMvc.perform(get("/members/exist/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("username", username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("data").value(false))
                .andDo(document("check-duplication-username",
                        requestParameters(
                                parameterWithName("username").description("중복 확인을 하려는 아이디")
                        ),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부"),
                                fieldWithPath("data").description("중복 여부")
                        )
                ));
    }

    @Autowired
    MemberRepository memberRepository;

    @Test
    @WithUserDetails
    void 회원조회() throws Exception {

        Member member = TestDataConfig.member;

        mockMvc.perform(get("/members/{username}", member.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("data.username").value(member.getUsername()))
                .andExpect(jsonPath("data.name").value(member.getName()))
                .andDo(document("get-member",
                        pathParameters(
                                parameterWithName("username").description("조회하려는 사용자 아이디")
                        ),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부"),
                                fieldWithPath("data.username").description("조회한 이용자 아이디"),
                                fieldWithPath("data.name").description("조회한 이용자 이름")
                        )
                ))
        ;
    }
}
