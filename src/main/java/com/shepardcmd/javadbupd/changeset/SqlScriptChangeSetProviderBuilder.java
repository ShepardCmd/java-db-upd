package com.shepardcmd.javadbupd.changeset;

public class SqlScriptChangeSetProviderBuilder {
    private boolean useResources = true;
    private String scriptDir;

    public SqlScriptChangeSetProviderBuilder setUseResources(boolean useResources) {
        this.useResources = useResources;
        return this;
    }

    public SqlScriptChangeSetProviderBuilder useScriptDir(String directory) {
        this.scriptDir = directory;
        return this;
    }

    public SqlScriptChangeSetProvider build() {
        return new SqlScriptChangeSetProvider(useResources, scriptDir);
    }
}
