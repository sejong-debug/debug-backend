package org.sj.capstone.debug.debugbackend.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static javax.servlet.http.HttpServletResponse.*;
import static org.sj.capstone.debug.debugbackend.error.ErrorCode.Domain.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_INPUT_VALUE(COMMON, SC_BAD_REQUEST, " Invalid Input Value."),
    INVALID_TYPE_VALUE(COMMON, SC_BAD_REQUEST,  " Invalid Type Value."),
    RESOURCE_NOT_FOUND(COMMON, SC_BAD_REQUEST, " Resource is Not Found."),
    NOT_OWNED_RESOURCE(COMMON, SC_FORBIDDEN, "Resource is not owned."),

    LOGIN_INPUT_INVALID(MEMBER, SC_BAD_REQUEST, "Login input is invalid."),
    USERNAME_DUPLICATION(MEMBER, SC_CONFLICT, "Username is Duplication."),

    IMAGE_IO_EXCEPTION(AI_CLIENT, SC_INTERNAL_SERVER_ERROR, "Error is in Image bytes."),
    ;

    private final Domain domain;

    private final int status;

    private final String message;

    enum Domain {
        COMMON,
        MEMBER,
        AI_CLIENT
    }
}
