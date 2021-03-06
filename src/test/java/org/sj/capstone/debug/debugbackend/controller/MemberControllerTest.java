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
    void ????????????_??????() throws Exception {
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
                                fieldWithPath("username").description("???????????? ID"),
                                fieldWithPath("password").description("???????????? ????????????"),
                                fieldWithPath("name").description("???????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("success").description("?????? ?????? ??????")
                        )
                ));
    }

    @Test
    void ?????????_????????????() throws Exception {
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
                                parameterWithName("username").description("?????? ????????? ????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("data").description("?????? ??????")
                        )
                ));
    }

    @Autowired
    MemberRepository memberRepository;

    @Test
    @WithUserDetails
    void ????????????() throws Exception {

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
                                parameterWithName("username").description("??????????????? ????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("data.username").description("????????? ????????? ?????????"),
                                fieldWithPath("data.name").description("????????? ????????? ??????")
                        )
                ))
        ;
    }
}
