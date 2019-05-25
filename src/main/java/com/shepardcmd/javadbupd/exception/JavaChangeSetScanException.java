package com.shepardcmd.javadbupd.exception;

public class JavaChangeSetScanException extends RuntimeException {
    public JavaChangeSetScanException() {
        super();
    }

    public JavaChangeSetScanException(String message) {
        super(message);
    }

    public JavaChangeSetScanException(String message, Throwable cause) {
        super(message, cause);
    }
}
