package com.shepardcmd.javadbupd;

import lombok.Getter;

public abstract class AbstractChangeSet {

    @Getter
    protected int version;

    public abstract UpdateResult execute();
}
