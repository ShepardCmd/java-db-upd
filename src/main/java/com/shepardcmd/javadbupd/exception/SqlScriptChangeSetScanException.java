package com.shepardcmd.javadbupd.exception;

public class SqlScriptChangeSetScanException extends RuntimeException {
    public SqlScriptChangeSetScanException() {
    }

    public SqlScriptChangeSetScanException(String message) {
        super(message);
    }

    public SqlScriptChangeSetScanException(String message, Throwable cause) {
        super(message, cause);
    }
}
