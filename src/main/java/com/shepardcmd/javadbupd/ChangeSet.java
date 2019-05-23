package com.shepardcmd.javadbupd;

public interface ChangeSet {

    int version();

    String name();

    void execute();
}
