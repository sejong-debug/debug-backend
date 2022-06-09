package org.sj.capstone.debug.debugbackend.dto.board;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class BoardUpdateDto {

    @NotNull
    @Size(max = 255)
    private String memo;
}
