package org.sj.capston.debug.debugbackend.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class JoinDto {

    @NotEmpty
    @Size(max = 45)
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String name;
}
