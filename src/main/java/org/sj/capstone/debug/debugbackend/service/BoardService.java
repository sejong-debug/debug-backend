package org.sj.capstone.debug.debugbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sj.capstone.debug.debugbackend.dto.board.BoardCreationDto;
import org.sj.capstone.debug.debugbackend.dto.board.BoardDto;
import org.sj.capstone.debug.debugbackend.dto.board.BoardIssueDto;
import org.sj.capstone.debug.debugbackend.dto.board.BoardUpdateDto;
import org.sj.capstone.debug.debugbackend.dto.issue.IssueDetectionDto;
import org.sj.capstone.debug.debugbackend.entity.*;
import org.sj.capstone.debug.debugbackend.error.ErrorCode;
import org.sj.capstone.debug.debugbackend.error.exception.BusinessException;
import org.sj.capstone.debug.debugbackend.repository.BoardImageRepository;
import org.sj.capstone.debug.debugbackend.repository.BoardRepository;
import org.sj.capstone.debug.debugbackend.repository.IssueRepository;
import org.sj.capstone.debug.debugbackend.repository.ProjectRepository;
import org.sj.capstone.debug.debugbackend.util.ImageStore;
import org.sj.capstone.debug.debugbackend.util.IssueConst;
import org.sj.capstone.debug.debugbackend.util.IssueDetectionClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardService {

    private final ProjectRepository projectRepository;

    private final BoardRepository boardRepository;

    private final BoardImageRepository boardImageRepository;

    private final IssueRepository issueRepository;

    private final ImageStore imageStore;

    private final IssueDetectionClient issueDetectionClient;

    private static final List<String> ISSUE_NAMES = List.of(
            "팥_흰가루병",
            "팥_세균잎마름병",
            "팥_Rhizopus",
            "참깨_세균성점무늬병",
            "참깨_흰가루병",
            "콩_노균병",
            "콩_들불병",
            "콩_불마름병",
            "콩_병징",
            "가지잎곰팡이병",
            "가지흰가루병",
            "고추마일드모틀바이러스병",
            "고추점무늬병",
            "단호박점무늬병",
            "단호박흰가루병",
            "딸기흰가루병",
            "상추균핵병",
            "상추노균병",
            "수박탄저병",
            "수박흰가루병",
            "애호박점무늬병",
            "오이녹반모자이크바이러스",
            "쥬키니호박 오이녹반모자이크바이러스",
            "참외노균병",
            "참외흰가루병",
            "토마토잎곰팡이병",
            "토마토황화잎말이바이러스병",
            "포도노균병",
            "고추흰가루병",
            "무검은무늬병",
            "무노균병",
            "배추검은썩음병",
            "배추노균병",
            "애호박노균병",
            "애호박흰가루병",
            "양배추균핵병",
            "양배추무름병",
            "오이노균병",
            "오이흰가루병",
            "콩점무늬병",
            "토마토잎마름병",
            "파검은무늬병",
            "파노균병",
            "파녹병",
            "호박노균병",
            "호박흰가루병",
            "배과수화상병",
            "사과갈색무늬병",
            "사과과수화상병",
            "사과점무늬낙엽병"
    );

    /**
     * TODO 로직 변경 -> AI Server 웹서버에서 이미지를 가져가도록 & 비동기
     * image 를 먼저 저장(storeImage)하면서 MultipartFile 내부가 변경되는듯
     * 이후 issue detection request 하면 에러 발생
     * 현재 임시로 request 를 맨위로 올리고 boardImageId 도 현재는 사용하지 않으므로 임시 값으로 넣어둠
     */
    @Transactional
    public long createBoard(BoardCreationDto creationDto, long projectId) throws IOException {
        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, ">> projectId=" + projectId));

        IssueDetectionDto issueDetectionDto = issueDetectionClient.request(
                1L, project.getCropType(), creationDto.getImage());

        BoardImage boardImage = imageStore.storeImage(creationDto.getImage());
        boardImageRepository.save(boardImage);

        Map<String, Double> issueNameToProbs = convertIssueProbListToNameProbMap(issueDetectionDto.getIssueProbs());
        Issue issue = Issue.builder()
                .boardImage(boardImage)
                .nameToProbs(issueNameToProbs)
                .build();
        issueRepository.save(issue);

        Board board = Board.builder()
                .project(project)
                .memo(creationDto.getMemo())
                .boardImage(boardImage)
                .build();
        return boardRepository.save(board).getId();
    }

    private Map<String, Double> convertIssueProbListToNameProbMap(List<Double> issueProbList) {
        return IntStream.range(0, Math.min(issueProbList.size(), ISSUE_NAMES.size())).boxed()
                .collect(Collectors.toMap(ISSUE_NAMES::get, issueProbList::get));
    }

    public Slice<BoardDto> getBoardSlice(Pageable pageable, long projectId) {
        return boardRepository.findAllByProjectId(pageable, projectId)
                .map(BoardDto::of);
    }

    public BoardIssueDto getBoard(long boardId) {
        BoardIssueDto boardIssueDto = boardRepository.findById(boardId)
                .map(BoardIssueDto::of)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, ">> boardId=" + boardId));
        List<String> issues = issueRepository.findByBoardId(boardIssueDto.getBoardId()).map(issue ->
                        issue.getNameToProbs().entrySet().stream()
                                .filter(entry -> entry.getValue() > IssueConst.CONFIDENCE_THRESHOLD)
                                .map(Map.Entry::getKey).collect(Collectors.toList()))
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Issue is not found >> boardId=" + boardId));
        boardIssueDto.setIssues(issues);
        return boardIssueDto;
    }

    @Transactional
    public long updateBoard(BoardUpdateDto updateDto, long boardId) {
        Board board = getBoardEntity(boardId);
        Board updatedBoard = Board.builder()
                .id(board.getId())
                .memo(updateDto.getMemo())
                .boardImage(board.getBoardImage())
                .build();
        return boardRepository.save(updatedBoard).getId();
    }

    public boolean isBoardNotOwnedByProject(long boardId, long projectId) {
        return !boardRepository.existsByIdAndProjectId(boardId, projectId);
    }

    private Board getBoardEntity(long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() ->
                new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Board is not found >> boardId=" + boardId));
    }
}
