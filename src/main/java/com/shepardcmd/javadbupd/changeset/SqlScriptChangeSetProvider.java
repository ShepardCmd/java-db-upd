package com.shepardcmd.javadbupd.changeset;

import com.shepardcmd.javadbupd.ChangeSet;
import com.shepardcmd.javadbupd.ChangeSetProvider;

import java.util.stream.Stream;

public class SqlScriptChangeSetProvider implements ChangeSetProvider {

    private boolean useResources = true;
    private String scriptDir;

    public SqlScriptChangeSetProvider setUseResources(boolean useResources) {
        this.useResources = useResources;
        return this;
    }

    public SqlScriptChangeSetProvider useScriptDir(String directory) {
        this.scriptDir = directory;
        return this;
    }

    @Override
    public Stream<ChangeSet> provideChangeSets() {
        return null;
    }
}
