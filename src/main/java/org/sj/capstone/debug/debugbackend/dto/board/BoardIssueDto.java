package org.sj.capstone.debug.debugbackend.dto.board;

import lombok.Data;
import org.sj.capstone.debug.debugbackend.entity.Board;

import java.net.URI;
import java.util.List;

@Data
public class BoardIssueDto {

    private long boardId;

    private String memo;

    private long boardImageId;

    private URI boardImageUri;

    private List<String> issues;

    public static BoardIssueDto of(Board board) {
        BoardIssueDto dto = new BoardIssueDto();
        dto.setBoardId(board.getId());
        dto.setMemo(board.getMemo());
        dto.setBoardImageId(board.getBoardImage().getId());
        return dto;
    }
}
