package io.hhplus.commerce.common.exception;

import lombok.Getter;

@Getter
public class CommerceException extends RuntimeException {
    private final CommerceErrorCodes errorCode;

    public CommerceException(CommerceErrorCodes errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CommerceException(CommerceErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
