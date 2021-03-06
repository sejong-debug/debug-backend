package org.sj.capstone.debug.debugbackend.controller;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
    @DisplayName("???????????? ?????? ?????? ??????")
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
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("data").description("???????????? ?????? ??????")
                        )
                ));
    }

    @Test
    @WithUserDetails
    @DisplayName("???????????? ??????")
    void createProjectTest() throws Exception {

        ProjectCreationDto creationDto = new ProjectCreationDto();
        creationDto.setName("????????? ????????????");
        creationDto.setStartDate(LocalDate.of(2022, 3, 1));
        creationDto.setEndDate(LocalDate.of(2022, 12, 1));
        creationDto.setCropType(CropType.???);

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("success").value(true))
                .andDo(document("create-project",
                        requestFields(
                                fieldWithPath("name").description("???????????? ??????"),
                                fieldWithPath("cropType").description("?????????????????? ????????? ?????? ??????"),
                                fieldWithPath("startDate").description("???????????? ?????? ??????"),
                                fieldWithPath("endDate").description("???????????? ?????? ?????? ??????")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("????????? ???????????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("data").description("????????? ???????????? ?????????")
                        )
                ));
    }

    @Test
    @WithUserDetails
    @DisplayName("???????????? ??????")
    void getProject() throws Exception {

        Project project = Project.builder()
                .member(TestDataConfig.member)
                .name("????????? ????????????")
                .cropType(CropType.???)
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
                                parameterWithName("projectId").description("??????????????? ???????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("data").description("????????? ???????????? ?????????"),
                                fieldWithPath("data.projectId").description("???????????? ?????????"),
                                fieldWithPath("data.name").description("???????????? ??????"),
                                fieldWithPath("data.cropType").description("?????????????????? ???????????? ?????? ??????"),
                                fieldWithPath("data.startDate").description("??????????????? ????????? ?????? ??????"),
                                fieldWithPath("data.endDate").description("??????????????? ????????? ?????? ??????"),
                                fieldWithPath("data.completed").description("???????????? ?????? ??????")
                        )
                ));
    }

    @Test
    @WithUserDetails
    @DisplayName("???????????? ??????")
    void updateProject() throws Exception {
        Project project = Project.builder()
                .member(TestDataConfig.member)
                .name("????????? ????????????")
                .cropType(CropType.???)
                .startDate(LocalDate.of(2022, 3, 1))
                .endDate(LocalDate.of(2022, 12, 1))
                .build();
        projectRepository.save(project);

        ProjectUpdateDto updateDto = new ProjectUpdateDto();
        updateDto.setName("?????? ????????????");
        updateDto.setCropType(CropType.??????);
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
                                parameterWithName("projectId").description("??????????????? ???????????? ?????????")
                        ),
                        requestFields(
                                fieldWithPath("name").description("???????????? ??????"),
                                fieldWithPath("cropType").description("?????????????????? ???????????? ?????? ??????"),
                                fieldWithPath("startDate").description("???????????? ?????? ??????"),
                                fieldWithPath("endDate").description("???????????? ?????? ??????"),
                                fieldWithPath("completed").description("???????????? ?????? ??????")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("????????? ???????????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("data").description("????????? ???????????? ?????????")
                        )
                ));
    }

    @Test
    @WithUserDetails
    @DisplayName("???????????? ????????? ???????????? ??????")
    void queryProject() throws Exception {

        createTestSampleProjects();

        mockMvc.perform(get("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andDo(document("query-projects",
                        requestParameters(
                                parameterWithName("page").description("??????????????? ????????? ?????????. default=0"),
                                parameterWithName("size").description("??????????????? ????????? ??? ????????? ??????. default=10, max=100")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("data").description("????????? ?????????"),
                                fieldWithPath("data.content").description("????????? ????????? ??????. Project ?????? ????????? ??????."),
                                fieldWithPath("data.number").description("????????? ??????")
                        )
                ));
    }

    private List<Project> createTestSampleProjects() {
        List<Project> projects = new ArrayList<>();
        LocalDate baseStartDate = LocalDate.of(2022, 3, 1);
        LocalDate baseEndDate = LocalDate.of(2022, 12, 1);
        for (int i = 0; i < 20; i++) {
            Project project = Project.builder()
                    .member(TestDataConfig.member)
                    .name("test project-" + i)
                    .cropType(CropType.values()[i % CropType.values().length])
                    .startDate(baseStartDate.plusDays((long) i * 10))
                    .endDate(baseEndDate.minusDays((long) i * 10))
                    .completed(i % 2 == 0)
                    .build();
            projects.add(project);
            projectRepository.save(project);
        }

        return projects;
    }

    @Test
    @WithUserDetails
    @DisplayName("???????????? ???????????? ??????")
    void updateProjectCompleted() throws Exception {

        Project project = TestDataConfig.project;
        mockMvc.perform(put("/projects/{projectId}/completed", project.getId())
                        .param("completed", String.valueOf(true))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("data").value(project.getId()))
                .andDo(document("update-project-completed",
                        requestParameters(
                                parameterWithName("completed").description("???????????? ?????? ??????")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("????????? ???????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("data").description("????????? ???????????? ?????????")
                        )
                ));
    }
}