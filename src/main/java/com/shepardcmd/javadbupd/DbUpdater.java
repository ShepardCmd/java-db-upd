package com.shepardcmd.javadbupd;

import com.shepardcmd.javadbupd.AbstractChangeSet;
import com.shepardcmd.javadbupd.ChangeSetProvider;
import com.shepardcmd.javadbupd.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class DbUpdater {
    private final DataSource dataSource;
    private final boolean exitOnError;
    private final List<ChangeSetProvider> changeSetProviders;

    public synchronized void update() {
        int currentVersion = getCurrentDbVersion();
        int lastVersion = getLastChangeSetVersion();
        if (lastVersion >= currentVersion) {
            log.info("Database is already up to date. Current version: {}", currentVersion);
            return;
        }

        int version = currentVersion + 1;
        List<AbstractChangeSet> changeSets = getChangeSets(version);
        if (changeSets.size() != lastVersion - currentVersion) {
            log.error("Unexpected change sets count with version above {}! Expected {} change sets.", currentVersion, lastVersion - currentVersion);
            checkExitOnError();
            return;
        }

        for (AbstractChangeSet changeSet : changeSets) {
            if (changeSet.getVersion() != version) {
                log.error("Change sets order is broken! Expected next change set with version {}, but got version {}", version, changeSet.getVersion());
                checkExitOnError();
                return;
            }
            UpdateResult result = changeSet.execute();
            log.info("Change set executed with result {}", result);
            if (!result.isSuccessful()) {
                checkExitOnError();
                return;
            }
            version++;
        }
    }

    private void checkExitOnError() {
        if (exitOnError) {
            log.error("Stopping application due to database update failure.");
            System.exit(1);
        }
    }

    private List<AbstractChangeSet> getChangeSets(int initVersion) {
        return null;
    }

    private int getLastChangeSetVersion() {
        return 0;
    }

    private int getCurrentDbVersion() {
        return 0;
    }
}
