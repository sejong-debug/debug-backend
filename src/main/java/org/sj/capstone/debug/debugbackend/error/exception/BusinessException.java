package org.sj.capstone.debug.debugbackend.error.exception;

import lombok.Getter;
import org.sj.capstone.debug.debugbackend.error.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode.name() + ":" + errorCode.getMessage() + " " + message);
        this.errorCode = errorCode;
    }
}
