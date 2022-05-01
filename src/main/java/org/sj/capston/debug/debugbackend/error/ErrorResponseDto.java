package org.sj.capston.debug.debugbackend.error;

import lombok.Data;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ErrorResponseDto {

    private int code;
    private String message;
    private List<FieldError> errors;

    public ErrorResponseDto(int code, String message) {
        this.code = code;
        this.message = message;
        this.errors = new ArrayList<>();
    }

    private ErrorResponseDto(final ErrorCode code, final List<FieldError> errors) {
        this.code = code.getStatus();
        this.message = code.getMessage();
        this.errors = errors;
    }

    private ErrorResponseDto(final ErrorCode code) {
        this.code = code.getStatus();
        this.message = code.getMessage();
        this.errors = new ArrayList<>();
    }

    public static ErrorResponseDto of(final ErrorCode code) {
        return new ErrorResponseDto(code);
    }

    public static ErrorResponseDto of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponseDto(code, FieldError.of(bindingResult));
    }

    public static ErrorResponseDto of(MethodArgumentTypeMismatchException ex) {
        final String value = ex.getValue() == null ? "" : ex.getValue().toString();
        final List<FieldError> errors = FieldError.of(ex.getName(), value, ex.getErrorCode());
        return ErrorResponseDto.of(ErrorCode.INVALID_TYPE_VALUE, errors);
    }

    public static ErrorResponseDto of(final ErrorCode code, final List<FieldError> errors) {
        return new ErrorResponseDto(code, errors);
    }

    @Data
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}