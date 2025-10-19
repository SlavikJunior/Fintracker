package com.slavikjunior.exceptions;

import jakarta.servlet.ServletException;

public class PageForwardException extends ServletException {

    private String message;

    public PageForwardException() {
    }

    public PageForwardException(String message) {
        super(message);
        this.message = message;
    }

    public PageForwardException(String message, Throwable rootCause) {
        super(message, rootCause);
        this.message = message;
    }

    public PageForwardException(Throwable rootCause) {
        super(rootCause);
    }
}
