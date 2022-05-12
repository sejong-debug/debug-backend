package org.sj.capstone.debug.debugbackend.dto.board;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class BoardCreationDto {

    @NotEmpty
    @Size(max = 255)
    private String content;

    @NotNull
    private MultipartFile image;
}
