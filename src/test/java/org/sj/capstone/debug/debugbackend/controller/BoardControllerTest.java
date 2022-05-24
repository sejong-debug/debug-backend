package org.sj.capstone.debug.debugbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sj.capstone.debug.debugbackend.common.RestDocsConfig;
import org.sj.capstone.debug.debugbackend.common.TestDataConfig;
import org.sj.capstone.debug.debugbackend.dto.board.BoardCreationDto;
import org.sj.capstone.debug.debugbackend.dto.board.BoardIssueDto;
import org.sj.capstone.debug.debugbackend.dto.board.BoardUpdateDto;
import org.sj.capstone.debug.debugbackend.entity.Board;
import org.sj.capstone.debug.debugbackend.entity.BoardImage;
import org.sj.capstone.debug.debugbackend.entity.Project;
import org.sj.capstone.debug.debugbackend.service.BoardService;
import org.sj.capstone.debug.debugbackend.util.IssueDetectionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import({RestDocsConfig.class, TestDataConfig.class})
@ActiveProfiles("test")
@Transactional
class BoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BoardService boardService;

    @Test
    @WithUserDetails
    @DisplayName("게시물 생성")
    void createBoard() throws Exception {

        Project project = TestDataConfig.project;
        MockMultipartFile image = new MockMultipartFile("image", "test-image.png",
                MediaType.IMAGE_PNG_VALUE, "test-image".getBytes());

        BoardCreationDto creationDto = new BoardCreationDto();
        creationDto.setImage(image);
        creationDto.setMemo("테스트 메모");
        given(boardService.createBoard(creationDto, project.getId())).willReturn(1L);

        mockMvc.perform(multipart("/projects/{projectId}/boards", project.getId())
                        .file(image)
                        .param("memo", creationDto.getMemo())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("success").value(true))
                .andDo(document("create-board",
                        pathParameters(
                                parameterWithName("projectId").description("게시물을 생성하려는 프로젝트 아이디")
                        ),
                        requestParameters(
                                parameterWithName("memo").description("게시물 메모")
                        ),
                        requestParts(
                                partWithName("image").description("게시물 작물 이미지")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("생성된 게시글 요청 경로")
                        ),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부"),
                                fieldWithPath("data").description("생성된 게시물 아이디")
                        )
                ));
    }

    @Test
    @WithUserDetails
    @DisplayName("게시물 조회")
    void getBoard() throws Exception {
        Project project = TestDataConfig.project;
        Board board = TestDataConfig.board;
        BoardImage boardImage = TestDataConfig.boardImage;

        BoardIssueDto boardIssueDto = new BoardIssueDto();
        boardIssueDto.setBoardId(board.getId());
        boardIssueDto.setBoardImageId(boardImage.getId());
        boardIssueDto.setMemo("테스트 게시물");
        boardIssueDto.setBoardImageUri(linkTo(BoardImageController.class).slash(boardImage.getId()).toUri());
        boardIssueDto.setIssues(List.of("질병1", "질병2", "질병3"));
        given(boardService.getBoard(board.getId())).willReturn(boardIssueDto);

        mockMvc.perform(get("/projects/{projectId}/boards/{boardId}", project.getId(), board.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-board",
                        pathParameters(
                                parameterWithName("projectId").description("조회하려는 게시물이 속한 프로젝트 아이디"),
                                parameterWithName("boardId").description("조회하려는 게시물 아이디")
                        ),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부"),
                                fieldWithPath("data").description("조회한 게시물 정보"),
                                fieldWithPath("data.boardId").description("조회한 게시물 아이디"),
                                fieldWithPath("data.memo").description("조회한 게시물 메모"),
                                fieldWithPath("data.boardImageId").description("조회한 게시물 이미지 아이디"),
                                fieldWithPath("data.boardImageUri").description("조회한 게시물 이미지 요청"),
                                fieldWithPath("data.issues").description("조회한 게시물 내 이슈 목록")
                        )
                ));
    }

    @Test
    @WithUserDetails
    @DisplayName("게시물 수정")
    void updateBoard() throws Exception {
        Project project = TestDataConfig.project;
        Board board = TestDataConfig.board;

        BoardUpdateDto boardUpdateDto = new BoardUpdateDto();
        boardUpdateDto.setMemo("update-memo");
        given(boardService.updateBoard(boardUpdateDto, board.getId())).willReturn(board.getId());

        mockMvc.perform(put("/projects/{projectId}/boards/{boardId}", project.getId(), board.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardUpdateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-board",
                        requestFields(
                                fieldWithPath("memo").description("수정하려는 게시물 메모 ")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("수정된 게시물 조회 요청")
                        ),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부"),
                                fieldWithPath("data").description("수정된 게시물 아이디")
                        )
                ));
    }
}