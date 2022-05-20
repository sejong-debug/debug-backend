package org.sj.capstone.debug.debugbackend.service;

import lombok.RequiredArgsConstructor;
import org.sj.capstone.debug.debugbackend.dto.board.BoardCreationDto;
import org.sj.capstone.debug.debugbackend.dto.board.BoardDto;
import org.sj.capstone.debug.debugbackend.dto.issue.IssueDetectionDto;
import org.sj.capstone.debug.debugbackend.entity.*;
import org.sj.capstone.debug.debugbackend.error.ErrorCode;
import org.sj.capstone.debug.debugbackend.error.exception.BusinessException;
import org.sj.capstone.debug.debugbackend.repository.BoardImageRepository;
import org.sj.capstone.debug.debugbackend.repository.BoardRepository;
import org.sj.capstone.debug.debugbackend.repository.IssueRepository;
import org.sj.capstone.debug.debugbackend.repository.ProjectRepository;
import org.sj.capstone.debug.debugbackend.util.ImageStore;
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
            "팥 정상",
            "참깨 정상",
            "콩 정상",
            "가지잎곰팡이병",
            "가지흰가루병",
            "고추마일드모틀바이러스병",
            "고추점무늬병",
            "단호박점무늬병",
            "단호박흰가루병",
            "딸기잿빛곰팡이병",
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
            "고추탄저병",
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
            "사과부란병",
            "사과점무늬낙엽병",
            "사과탄저병",
            "가지 정상",
            "감자 정상",
            "고추 정상",
            "단호박 정상",
            "들깨 정상",
            "딸기 정상",
            "무 정상",
            "배 정상",
            "배추 정상",
            "벼 정상",
            "사과 정상",
            "상추 정상",
            "수박 정상",
            "애호박 정상",
            "양배추 정상",
            "오이 정상",
            "옥수수 정상",
            "쥬키니호박 정상",
            "참외 정상",
            "토마토 정상",
            "파 정상",
            "포도 정상",
            "호박 정상",
            "검거세미밤나방",
            "꽃노랑총채벌레",
            "담배가루이",
            "담배거세미나방",
            "담배나방",
            "도둑나방",
            "먹노린재",
            "목화바둑명나방",
            "무잎벌",
            "배추좀나방",
            "배추흰나비",
            "벼룩잎벌레",
            "복숭아혹진딧물",
            "비단노린재",
            "썩덩나무노린재",
            "열대거세미나방",
            "큰28점박이무당벌레",
            "톱다리개미허리노린재",
            "파밤나방"
    );

    @Transactional
    public long createBoard(BoardCreationDto creationDto, long projectId) throws IOException {
        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, ">> projectId=" + projectId));
        BoardImage boardImage = imageStore.storeImage(creationDto.getImage());
        boardImageRepository.save(boardImage);

        IssueDetectionDto issueDetectionDto = issueDetectionClient.request(boardImage.getId(), creationDto.getImage());
        Map<String, Double> issueNameToProbs = convertIssueProbListToNameProbMap(issueDetectionDto.getIssueProbs());
        Issue issue = Issue.builder()
                .boardImage(boardImage)
                .nameToProbs(issueNameToProbs)
                .build();

        Board board = Board.builder()
                .project(project)
                .memo(creationDto.getMemo())
                .boardImage(boardImage)
                .build();

        issueRepository.save(issue);
        return boardRepository.save(board).getId();
    }

    private Map<String, Double> convertIssueProbListToNameProbMap(List<Double> issueProbList) {
        return IntStream.rangeClosed(0, issueProbList.size()).boxed()
                .collect(Collectors.toMap(ISSUE_NAMES::get, issueProbList::get));
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
