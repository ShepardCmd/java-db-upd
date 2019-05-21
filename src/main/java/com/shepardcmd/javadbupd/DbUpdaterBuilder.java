package com.shepardcmd.javadbupd;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class DbUpdaterBuilder {
    private boolean exitOnError = true;
    private String historyTableName = "db_history";
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

    public DbUpdaterBuilder setHistoryTableName(String historyTableName) {
        this.historyTableName = historyTableName;
        return this;
    }

    public DbUpdater build() {
        if (changeSetProviders.isEmpty()) {
            throw new IllegalStateException("You must set at least one ChangeSetProvider!");
        }
        if (dataSource == null) {
            throw new IllegalStateException("You must provide a DataSource!");
        }
        if (historyTableName == null || historyTableName.trim().isEmpty()) {
            throw new IllegalStateException("History table name must not be null or empty! Whether specify valid table name or don't override default value.");
        }
        return new DbUpdater(dataSource, exitOnError, historyTableName, changeSetProviders);
    }
}
