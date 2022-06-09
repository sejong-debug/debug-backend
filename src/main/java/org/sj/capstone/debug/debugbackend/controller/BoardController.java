package org.sj.capstone.debug.debugbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sj.capstone.debug.debugbackend.dto.board.BoardCreationDto;
import org.sj.capstone.debug.debugbackend.dto.board.BoardDto;
import org.sj.capstone.debug.debugbackend.dto.board.BoardIssueDto;
import org.sj.capstone.debug.debugbackend.dto.board.BoardUpdateDto;
import org.sj.capstone.debug.debugbackend.dto.common.ApiResult;
import org.sj.capstone.debug.debugbackend.error.ErrorCode;
import org.sj.capstone.debug.debugbackend.error.exception.BusinessException;
import org.sj.capstone.debug.debugbackend.security.LoginMemberId;
import org.sj.capstone.debug.debugbackend.service.BoardService;
import org.sj.capstone.debug.debugbackend.service.ProjectService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

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
                .created((linkTo(methodOn(BoardController.class).getBoard(projectId, boardId, memberId))).toUri())
                .body(result);
    }

    @GetMapping
    public ResponseEntity<ApiResult<Slice<BoardDto>>> queryBoards(
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable long projectId) {
        Slice<BoardDto> boardSlice = boardService.getBoardSlice(pageable, projectId)
                .map(boardDto -> {
                    boardDto.setBoardImageUri(createBoardImageUri(boardDto.getBoardImageId()));
                    return boardDto;
                });
        ApiResult<Slice<BoardDto>> result = ApiResult.<Slice<BoardDto>>builder()
                .data(boardSlice)
                .build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResult<BoardIssueDto>> getBoard(
            @PathVariable long projectId, @PathVariable long boardId, @LoginMemberId Long memberId) {
        if (projectService.isProjectNotOwnedByMember(projectId, memberId)) {
            throw new BusinessException(ErrorCode.NOT_OWNED_RESOURCE,
                    ">> projectId=" + projectId + ", memberId=" + memberId);
        }
        BoardIssueDto boardIssueDto = boardService.getBoard(boardId);
        boardIssueDto.setBoardImageUri(createBoardImageUri(boardIssueDto.getBoardImageId()));
        ApiResult<BoardIssueDto> result = ApiResult.<BoardIssueDto>builder()
                .data(boardIssueDto)
                .build();

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<ApiResult<Long>> updateBoard(
            @RequestBody @Valid BoardUpdateDto updateDto, @PathVariable long projectId, @PathVariable long boardId,
            @LoginMemberId Long memberId) {
        if (projectService.isProjectNotOwnedByMember(projectId, memberId) ||
                boardService.isBoardNotOwnedByProject(boardId, projectId)) {
            throw new BusinessException(ErrorCode.NOT_OWNED_RESOURCE,
                    ">> memberId=" + memberId + ", projectId=" + projectId + ", boardId=" + boardId);
        }

        ApiResult<Long> result = ApiResult.<Long>builder()
                .data(boardService.updateBoard(updateDto, boardId))
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .location(linkTo(methodOn(BoardController.class).getBoard(projectId, boardId, memberId)).toUri())
                .body(result);
    }

    private URI createBoardImageUri(long boardImageId) {
        return linkTo(BoardImageController.class).slash(boardImageId).toUri();
    }
}
