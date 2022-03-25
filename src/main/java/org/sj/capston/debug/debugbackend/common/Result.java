package org.sj.capston.debug.debugbackend.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    private boolean success;

    T data;

    private String errorCode;
}
