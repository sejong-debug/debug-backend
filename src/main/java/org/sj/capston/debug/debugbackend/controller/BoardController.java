package org.sj.capstone.debug.debugbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sj.capstone.debug.debugbackend.dto.BoardCreationDto;
import org.sj.capstone.debug.debugbackend.dto.BoardDto;
import org.sj.capstone.debug.debugbackend.service.BoardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/projects/{projectId}/boards")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Void> createBoard(
            @Validated @ModelAttribute BoardCreationDto boardCreationDto,
            @PathVariable long projectId) throws IOException {

        long boardId = boardService.createBoard(boardCreationDto, projectId);

        return ResponseEntity
                .created((linkTo(methodOn(BoardController.class).getBoard(boardId))).toUri())
                .build();
    }

    @GetMapping
    public ResponseEntity<Slice<BoardDto>> queryBoards(
            @PathVariable long projectId, Pageable pageable) {
        return ResponseEntity.ok(boardService.getBoardSlice(pageable, projectId));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable long boardId) {
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }
}
