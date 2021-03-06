package org.sj.capstone.debug.debugbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sj.capstone.debug.debugbackend.common.RestDocsConfig;
import org.sj.capstone.debug.debugbackend.common.TestDataConfig;
import org.sj.capstone.debug.debugbackend.dto.board.BoardCreationDto;
import org.sj.capstone.debug.debugbackend.dto.board.BoardDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    @DisplayName("????????? ??????")
    void createBoard() throws Exception {

        Project project = TestDataConfig.project;
        MockMultipartFile image = new MockMultipartFile("image", "test-image.png",
                MediaType.IMAGE_PNG_VALUE, "test-image".getBytes());

        BoardCreationDto creationDto = new BoardCreationDto();
        creationDto.setImage(image);
        creationDto.setMemo("????????? ??????");
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
                                parameterWithName("projectId").description("???????????? ??????????????? ???????????? ?????????")
                        ),
                        requestParameters(
                                parameterWithName("memo").description("????????? ??????")
                        ),
                        requestParts(
                                partWithName("image").description("????????? ?????? ?????????")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("????????? ????????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("data").description("????????? ????????? ?????????")
                        )
                ));
    }

    @Test
    @WithUserDetails
    @DisplayName("????????? ??????")
    void getBoard() throws Exception {
        Project project = TestDataConfig.project;
        Board board = TestDataConfig.board;
        BoardImage boardImage = TestDataConfig.boardImage;

        BoardIssueDto boardIssueDto = new BoardIssueDto();
        boardIssueDto.setBoardId(board.getId());
        boardIssueDto.setBoardImageId(boardImage.getId());
        boardIssueDto.setMemo("????????? ?????????");
        boardIssueDto.setBoardImageUri(linkTo(BoardImageController.class).slash(boardImage.getId()).toUri());
        boardIssueDto.setIssues(List.of("??????1", "??????2", "??????3"));
        given(boardService.getBoard(board.getId())).willReturn(boardIssueDto);

        mockMvc.perform(get("/projects/{projectId}/boards/{boardId}", project.getId(), board.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-board",
                        pathParameters(
                                parameterWithName("projectId").description("??????????????? ???????????? ?????? ???????????? ?????????"),
                                parameterWithName("boardId").description("??????????????? ????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("data").description("????????? ????????? ??????"),
                                fieldWithPath("data.boardId").description("????????? ????????? ?????????"),
                                fieldWithPath("data.memo").description("????????? ????????? ??????"),
                                fieldWithPath("data.boardImageId").description("????????? ????????? ????????? ?????????"),
                                fieldWithPath("data.boardImageUri").description("????????? ????????? ????????? ??????"),
                                fieldWithPath("data.issues").description("????????? ????????? ??? ?????? ??????")
                        )
                ));
    }

    @Test
    @WithUserDetails
    @DisplayName("????????? ??????")
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
                                fieldWithPath("memo").description("??????????????? ????????? ?????? ")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("????????? ????????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("data").description("????????? ????????? ?????????")
                        )
                ));
    }

    @Test
    @WithUserDetails
    @DisplayName("????????? ????????? ???????????? ??????")
    void queryBoards() throws Exception {
        Project project = TestDataConfig.project;
        Board board = TestDataConfig.board;
        BoardDto boardDto = new BoardDto();
        boardDto.setBoardId(board.getId());
        boardDto.setMemo(board.getMemo());
        boardDto.setBoardImageId(board.getBoardImage().getId());

        Pageable pageable = PageRequest.of(0, 10);
        given(boardService.getBoardSlice(pageable, project.getId()))
                .willReturn(new SliceImpl<>(List.of(boardDto), pageable, false));

        mockMvc.perform(get("/projects/{projectId}/boards", project.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andDo(document("query-boards",
                        requestParameters(
                                parameterWithName("page").description("??????????????? ????????? ?????????. default=0"),
                                parameterWithName("size").description("??????????????? ????????? ??? ????????? ??????. default=10, max=100")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("data").description("????????? ?????????"),
                                fieldWithPath("data.content").description("????????? ????????? ??????. Board ?????? ????????? ??????. issues ??????"),
                                fieldWithPath("data.number").description("????????? ??????")
                        )
                ));
    }
}