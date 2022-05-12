package org.sj.capstone.debug.debugbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sj.capstone.debug.debugbackend.common.RestDocsConfig;
import org.sj.capstone.debug.debugbackend.common.TestDataConfig;
import org.sj.capstone.debug.debugbackend.dto.project.ProjectCreationDto;
import org.sj.capstone.debug.debugbackend.dto.project.ProjectUpdateDto;
import org.sj.capstone.debug.debugbackend.entity.CropType;
import org.sj.capstone.debug.debugbackend.entity.Project;
import org.sj.capstone.debug.debugbackend.repository.ProjectRepository;
import org.sj.capstone.debug.debugbackend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import({RestDocsConfig.class, TestDataConfig.class})
@ActiveProfiles("test")
@Transactional
class ProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberService memberService;

    @Autowired
    ProjectRepository projectRepository;

    @Test
    @WithUserDetails
    @DisplayName("프로젝트 작물 종류 조회")
    void getAllCropTypesTest() throws Exception {

        mockMvc.perform(get("/projects/crop-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("data").value(
                        Arrays.stream(CropType.values())
                                .map(Enum::name)
                                .collect(Collectors.toList())))
                .andDo(document("get-crop-types",
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부"),
                                fieldWithPath("data").description("프로젝트 작물 종류")
                        )
                ));
    }

    @Test
    @WithUserDetails
    @DisplayName("프로젝트 생성")
    void createProjectTest() throws Exception {

        ProjectCreationDto creationDto = new ProjectCreationDto();
        creationDto.setName("테스트 프로젝트");
        creationDto.setStartDate(LocalDate.of(2022, 3, 1));
        creationDto.setEndDate(LocalDate.of(2022, 12, 1));
        creationDto.setCropType(CropType.팥);

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("success").value(true))
                .andDo(document("create-project",
                        requestFields(
                                fieldWithPath("name").description("프로젝트 이름"),
                                fieldWithPath("cropType").description("프로젝트에서 관리할 작물 종류"),
                                fieldWithPath("startDate").description("프로젝트 시작 날짜"),
                                fieldWithPath("endDate").description("프로젝트 종료 예상 날짜"))
                        ,
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("생성된 프로젝트 조회 요청")
                        ),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부"),
                                fieldWithPath("data").description("생성된 프로젝트 아이디")
                        )
                ));
    }

    @Test
    @WithUserDetails
    @DisplayName("프로젝트 조회")
    void getProject() throws Exception {

        Project project = Project.builder()
                .member(TestDataConfig.member)
                .name("테스트 프로젝트")
                .cropType(CropType.팥)
                .startDate(LocalDate.of(2022, 3, 1))
                .endDate(LocalDate.of(2022, 12, 1))
                .build();
        projectRepository.save(project);

        mockMvc.perform(get("/projects/{projectId}", project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("data.projectId").value(project.getId()))
                .andExpect(jsonPath("data.name").value(project.getName()))
                .andExpect(jsonPath("data.cropType").value(project.getCropType().name()))
                .andExpect(jsonPath("data.startDate").value(project.getStartDate().toString()))
                .andExpect(jsonPath("data.endDate").value(project.getEndDate().toString()))
                .andExpect(jsonPath("data.completed").value(project.isCompleted()))
                .andDo(document("get-project",
                        pathParameters(
                                parameterWithName("projectId").description("조회하려는 프로젝트 아이디")
                        ),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부"),
                                fieldWithPath("data").description("조회한 프로젝트 데이터"),
                                fieldWithPath("data.projectId").description("프로젝트 아이디"),
                                fieldWithPath("data.name").description("프로젝트 이름"),
                                fieldWithPath("data.cropType").description("프로젝트에서 관리하는 작물 종류"),
                                fieldWithPath("data.startDate").description("프로젝트에 설정한 시작 날짜"),
                                fieldWithPath("data.endDate").description("프로젝트에 설정한 종료 날짜"),
                                fieldWithPath("data.completed").description("프로젝트 완료 여부")
                        )
                ));
    }

    @Test
    @WithUserDetails
    @DisplayName("프로젝트 수정")
    void updateProject() throws Exception {
        Project project = Project.builder()
                .member(TestDataConfig.member)
                .name("테스트 프로젝트")
                .cropType(CropType.팥)
                .startDate(LocalDate.of(2022, 3, 1))
                .endDate(LocalDate.of(2022, 12, 1))
                .build();
        projectRepository.save(project);

        ProjectUpdateDto updateDto = new ProjectUpdateDto();
        updateDto.setName("수정 프로젝트");
        updateDto.setCropType(CropType.가지);
        updateDto.setStartDate(null);
        updateDto.setEndDate(null);
        updateDto.setCompleted(false);

        mockMvc.perform(put("/projects/{projectId}", project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("data").value(project.getId()))
                .andDo(document("update-project",
                        pathParameters(
                                parameterWithName("projectId").description("수정하려는 프로젝트 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("프로젝트 이름"),
                                fieldWithPath("cropType").description("프로젝트에서 관리하는 작물 종류"),
                                fieldWithPath("startDate").description("프로젝트 시작 날짜"),
                                fieldWithPath("endDate").description("프로젝트 종료 날짜"),
                                fieldWithPath("completed").description("프로젝트 완료 여부")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("수정된 프로젝트 조회 요청")
                        ),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부"),
                                fieldWithPath("data").description("수정된 프로젝트 아이디")
                        )
                ));
    }
}