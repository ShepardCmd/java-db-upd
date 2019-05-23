package com.shepardcmd.javadbupd.changeset;

import com.shepardcmd.javadbupd.ChangeSet;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
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
    public void execute() {
        javaChangeSet.execute(dependencies);
    }
}
