package org.sj.capston.debug.debugbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.sj.capston.debug.debugbackend.common.BaseControllerTest;
import org.sj.capston.debug.debugbackend.common.RestDocsConfig;
import org.sj.capston.debug.debugbackend.dto.JoinDto;
import org.sj.capston.debug.debugbackend.dto.LoginDto;
import org.sj.capston.debug.debugbackend.entity.Member;
import org.sj.capston.debug.debugbackend.repository.MemberRepository;
import org.sj.capston.debug.debugbackend.service.MemberService;
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
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfig.class)
@ActiveProfiles("test")
@Transactional
class IndexControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void index() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 회원가입_성공() throws Exception {
        String username = "test-username";
        String password = "test-password!";
        String name = "test-name";

        JoinDto joinDto = new JoinDto();
        joinDto.setUsername(username);
        joinDto.setPassword(password);
        joinDto.setName(name);

        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(joinDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andDo(document("join",
                        requestFields(
                                fieldWithPath("username").description("회원가입 ID"),
                                fieldWithPath("password").description("회원가입 비밀번호"),
                                fieldWithPath("name").description("회원가입 이름")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("회원가입 후 index 요청을 받아 login 요청")
                        )
                ));

        Member joinedMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("회원가입 실패"));
        assertThat(joinedMember.getUsername()).isEqualTo(username);
        assertThat(joinedMember.getName()).isEqualTo(name);
    }
    
    @Test
    void 로그인_성공() throws Exception {
        JoinDto joinDto = join();

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(joinDto.getUsername());
        loginDto.setPassword(joinDto.getPassword());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andDo(print())
                .andDo(document("login",
                        responseFields(
                                fieldWithPath("token").description("JWT 로그인 토큰")
                        )));
    }

    private JoinDto join() {
        String username = "test-username";
        String password = "test-password!";
        String name = "test-name";

        JoinDto joinDto = new JoinDto();
        joinDto.setUsername(username);
        joinDto.setPassword(password);
        joinDto.setName(name);
        memberService.join(joinDto);

        return joinDto;
    }
}