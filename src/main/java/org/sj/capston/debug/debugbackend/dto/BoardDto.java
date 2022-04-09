package org.sj.capston.debug.debugbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.sj.capston.debug.debugbackend.entity.Board;

@Data
@Builder
@AllArgsConstructor
public class BoardDto {

    private long boardId;

    private String content;

    private long boardImageId;

    public static BoardDto of(Board board) {
        return BoardDto.builder()
                .boardId(board.getId())
                .content(board.getContent())
                .boardImageId(board.getBoardImage().getId())
                .build();
    }
}
