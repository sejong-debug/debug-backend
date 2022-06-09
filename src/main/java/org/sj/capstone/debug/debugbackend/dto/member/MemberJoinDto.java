package org.sj.capstone.debug.debugbackend.dto.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class MemberJoinDto {

    @NotEmpty
    @Size(max = 45)
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String name;
}
