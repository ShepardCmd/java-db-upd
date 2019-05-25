package com.shepardcmd.javadbupd;

import com.shepardcmd.javadbupd.changeset.JavaChangeSetProviderBuilder;
import com.shepardcmd.javadbupd.changeset.SqlScriptChangeSetProviderBuilder;

public class ChangeSetProviders {
    private ChangeSetProviders() { }

    public static SqlScriptChangeSetProviderBuilder newSqlScriptChangeSetProvider() {
        return new SqlScriptChangeSetProviderBuilder();
    }

    public static JavaChangeSetProviderBuilder newJavaChangeSetProvider() {
        return new JavaChangeSetProviderBuilder();
    }
}
