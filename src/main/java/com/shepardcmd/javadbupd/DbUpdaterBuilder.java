package com.shepardcmd.javadbupd;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class DbUpdaterBuilder {
    private boolean exitOnError = true;
    private DataSource dataSource;
    private List<ChangeSetProvider> changeSetProviders = new ArrayList<>(2);

    public DbUpdaterBuilder setExitOnError(boolean exitOnError) {
        this.exitOnError = exitOnError;
        return this;
    }

    public DbUpdaterBuilder setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public DbUpdaterBuilder addChangeSetProvider(ChangeSetProvider changeSetProvider) {
        changeSetProviders.add(changeSetProvider);
        return this;
    }

    public DbUpdater build() {
        if (changeSetProviders.isEmpty()) {
            throw new IllegalStateException("You must set at least one ChangeSetProvider!");
        }
        if (dataSource == null) {
            throw new IllegalStateException("You must provide a DataSource!");
        }
        return new DbUpdater(dataSource, exitOnError, changeSetProviders);
    }
}
