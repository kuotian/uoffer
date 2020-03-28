package com.hxxzt.recruitment.common.exception;


public class PenintTokenException extends Exception {
    private static final long serialVersionUID = -994962710559017255L;

    public PenintTokenException(String message) {
        super(message);
    }

    public PenintTokenException(Throwable e) {
        super(e.getMessage(), e);
    }
}