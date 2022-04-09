package org.sj.capston.debug.debugbackend.service;

import lombok.RequiredArgsConstructor;
import org.sj.capston.debug.debugbackend.dto.BoardCreationDto;
import org.sj.capston.debug.debugbackend.dto.BoardDto;
import org.sj.capston.debug.debugbackend.entity.Board;
import org.sj.capston.debug.debugbackend.entity.BoardImage;
import org.sj.capston.debug.debugbackend.entity.Project;
import org.sj.capston.debug.debugbackend.repository.BoardImageRepository;
import org.sj.capston.debug.debugbackend.repository.BoardRepository;
import org.sj.capston.debug.debugbackend.repository.ProjectRepository;
import org.sj.capston.debug.debugbackend.util.ImageStore;
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
