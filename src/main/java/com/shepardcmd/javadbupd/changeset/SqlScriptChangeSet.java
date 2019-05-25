package com.shepardcmd.javadbupd.changeset;

import com.shepardcmd.javadbupd.ChangeSet;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.sql.Connection;

@RequiredArgsConstructor
@ToString
public class SqlScriptChangeSet implements ChangeSet {
    private final String scriptUrl;
    private final boolean fromResources;
    private final int version;


    @Override
    public int version() {
        return version;
    }

    @Override
    public String name() {
        return scriptUrl;
    }

    @Override
    public boolean execute(Connection connection) {
        return false;
    }
}
