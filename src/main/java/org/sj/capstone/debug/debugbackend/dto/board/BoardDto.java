package org.sj.capstone.debug.debugbackend.dto.board;

import lombok.Data;
import org.sj.capstone.debug.debugbackend.entity.Board;

import java.net.URI;

@Data
public class BoardDto {

    private long boardId;

    private String memo;

    private long boardImageId;

    private URI boardImageUri;

    public static BoardDto of(Board board) {
        BoardDto boardDto = new BoardDto();
        boardDto.setBoardId(board.getId());
        boardDto.setMemo(board.getMemo());
        boardDto.setBoardImageId(board.getBoardImage().getId());
        return boardDto;
    }
}
