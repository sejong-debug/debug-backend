package org.sj.capstone.debug.debugbackend.service;

import lombok.RequiredArgsConstructor;
import org.sj.capstone.debug.debugbackend.dto.board.BoardCreationDto;
import org.sj.capstone.debug.debugbackend.dto.board.BoardDto;
import org.sj.capstone.debug.debugbackend.repository.BoardImageRepository;
import org.sj.capstone.debug.debugbackend.repository.BoardRepository;
import org.sj.capstone.debug.debugbackend.repository.ProjectRepository;
import org.sj.capstone.debug.debugbackend.util.ImageStore;
import org.sj.capstone.debug.debugbackend.entity.Board;
import org.sj.capstone.debug.debugbackend.entity.BoardImage;
import org.sj.capstone.debug.debugbackend.entity.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final ProjectRepository projectRepository;

    private final BoardRepository boardRepository;

    private final BoardImageRepository boardImageRepository;

    private final ImageStore imageStore;

    @Transactional
    public long createBoard(BoardCreationDto creationDto, long projectId) throws IOException {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 projectId = " + projectId));
        BoardImage boardImage = imageStore.storeImage(creationDto.getImage());
        boardImageRepository.save(boardImage);

        Board board = Board.builder()
                .project(project)
                .content(creationDto.getContent())
                .boardImage(boardImage)
                .build();

        return boardRepository.save(board).getId();
    }

    public Slice<BoardDto> getBoardSlice(Pageable pageable, long projectId) {
        return boardRepository.findAllByProjectId(pageable, projectId)
                .map(BoardDto::of);
    }

    public BoardDto getBoard(long boardId) {
        return boardRepository.findById(boardId)
                .map(BoardDto::of)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 boardId = " + boardId));
    }
}
