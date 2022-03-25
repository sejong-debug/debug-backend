package org.sj.capston.debug.debugbackend.hello;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class HelloDto {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    public HelloDto(@NonNull Hello hello) {
        this.name = hello.getName();
        this.description = hello.getDescription();
    }
}
