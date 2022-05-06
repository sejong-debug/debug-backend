package org.sj.capstone.debug.debugbackend.dto.security;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginDto {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
