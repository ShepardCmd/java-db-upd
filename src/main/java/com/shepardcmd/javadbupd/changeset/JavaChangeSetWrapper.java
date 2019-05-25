package com.shepardcmd.javadbupd.changeset;

import com.shepardcmd.javadbupd.ChangeSet;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.sql.Connection;
import java.util.Map;

@RequiredArgsConstructor
@ToString
public class JavaChangeSetWrapper implements ChangeSet {
    private final JavaChangeSet javaChangeSet;
    private final Map<String, Object> dependencies;

    @Override
    public int version() {
        return javaChangeSet.version();
    }

    @Override
    public String name() {
        return javaChangeSet.getClass().getName();
    }

    @Override
    public boolean execute(Connection connection) {
        return javaChangeSet.execute(connection, dependencies);
    }
}
