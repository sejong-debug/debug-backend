package org.sj.capstone.debug.debugbackend.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.sj.capstone.debug.debugbackend.entity.Board;

import java.net.URI;

@Data
@Builder
@AllArgsConstructor
public class BoardDto {

    private long boardId;

    private String memo;

    private long boardImageId;

    private URI boardImageUri;

    public static BoardDto of(Board board) {
        return BoardDto.builder()
                .boardId(board.getId())
                .memo(board.getMemo())
                .boardImageId(board.getBoardImage().getId())
                .build();
    }
}
