package com.shepardcmd.javadbupd.changeset;

import com.shepardcmd.javadbupd.ChangeSet;
import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;

@AllArgsConstructor
public class SqlScriptChangeSet implements ChangeSet {
    private final Connection connection;
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
    public void execute() {

    }
}
