package org.sj.capston.debug.debugbackend.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginDto {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
