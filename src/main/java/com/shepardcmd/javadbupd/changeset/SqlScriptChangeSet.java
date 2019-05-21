package com.shepardcmd.javadbupd.changeset;

import com.shepardcmd.javadbupd.AbstractChangeSet;
import com.shepardcmd.javadbupd.UpdateResult;
import lombok.AllArgsConstructor;

import javax.sql.DataSource;

@AllArgsConstructor
public class SqlScriptChangeSet extends AbstractChangeSet {
    private final DataSource dataSource;
    private final String scriptUrl;
    private final boolean fromResources;

    public UpdateResult execute() {
        return null;
    }
}
