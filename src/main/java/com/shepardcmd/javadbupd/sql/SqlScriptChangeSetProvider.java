package com.shepardcmd.javadbupd.sql;

import com.shepardcmd.javadbupd.AbstractChangeSet;
import com.shepardcmd.javadbupd.ChangeSetProvider;
import com.shepardcmd.javadbupd.DbUpdaterBuilder;

import java.util.List;

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
    public List<AbstractChangeSet> provideChangeSets() {
        return null;
    }
}
