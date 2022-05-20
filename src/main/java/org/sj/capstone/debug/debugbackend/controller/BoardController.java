package org.sj.capstone.debug.debugbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sj.capstone.debug.debugbackend.dto.board.BoardCreationDto;
import org.sj.capstone.debug.debugbackend.dto.board.BoardDto;
import org.sj.capstone.debug.debugbackend.dto.common.ApiResult;
import org.sj.capstone.debug.debugbackend.error.ErrorCode;
import org.sj.capstone.debug.debugbackend.error.exception.BusinessException;
import org.sj.capstone.debug.debugbackend.security.LoginMemberId;
import org.sj.capstone.debug.debugbackend.service.BoardService;
import org.sj.capstone.debug.debugbackend.service.ProjectService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/projects/{projectId}/boards")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ApiResult<Long>> createBoard(
            @Valid @ModelAttribute BoardCreationDto boardCreationDto,
            @PathVariable long projectId, @LoginMemberId Long memberId) throws IOException {
        if (projectService.isProjectNotOwnedByMember(projectId, memberId)) {
            throw new BusinessException(ErrorCode.NOT_OWNED_RESOURCE,
                    ">> projectId=" + projectId + ", memberId=" + memberId);
        }
        long boardId = boardService.createBoard(boardCreationDto, projectId);
        ApiResult<Long> result = ApiResult.<Long>builder()
                .data(boardId)
                .build();

        return ResponseEntity
                .created((linkTo(methodOn(BoardController.class).getBoard(projectId, boardId))).toUri())
                .body(result);
    }

    @GetMapping
    public ResponseEntity<ApiResult<Slice<BoardDto>>> queryBoards(
            @PathVariable long projectId, Pageable pageable) {
        Slice<BoardDto> boardSlice = boardService.getBoardSlice(pageable, projectId)
                .map(boardDto -> {
                    boardDto.setBoardImageUri(
                            linkTo(BoardImageController.class).slash(boardDto.getBoardImageId()).toUri());
                    return boardDto;
                });
        ApiResult<Slice<BoardDto>> result = ApiResult.<Slice<BoardDto>>builder()
                .data(boardSlice)
                .build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable long projectId, @PathVariable long boardId) {
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }
}
