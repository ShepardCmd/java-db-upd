package com.shepardcmd.javadbupd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class DbUpdater {
    private final DataSource dataSource;
    private final boolean exitOnError;
    private final String historyTableName;
    private final List<ChangeSetProvider> changeSetProviders;

    public synchronized void update() {
        log.info("Starting database update...");
        try (Connection connection = dataSource.getConnection()) {
            HistoryWriter historyWriter = new HistoryWriter(historyTableName, connection);
            historyWriter.prepareHistoryTable();

            int currentVersion = historyWriter.getCurrentDbVersion();
            List<ChangeSet> changeSets = getChangeSets(currentVersion + 1);
            if (changeSets == null || changeSets.isEmpty()) {
                log.info("Database is already up to date, current version: {}", currentVersion);
                return;
            }
            if (!validateVersions(changeSets, currentVersion)) {
                log.error("Change sets versions order is broken! Please, make sure that all change set versions are positive integers, and there are no gaps between versions.");
                checkExitOnError();
                return;
            }
            for (ChangeSet changeSet : changeSets) {
                log.debug("Running change set {} with version {}", changeSet.name(), changeSet.version());
                Date startTime = new Date();
                try {
                    changeSet.execute();
                    historyWriter.saveUpdateResult(new UpdateResult(changeSet.version(), changeSet.name(), startTime, new Date(), true));
                } catch (Throwable t) {
                    log.error("Error during change set {} execution: {}", changeSet.name(), t);
                    historyWriter.saveUpdateResult(new UpdateResult(changeSet.version(), changeSet.name(), startTime, new Date(), false));
                    checkExitOnError();
                    return;
                }
                log.debug("Successfully executed change set {} with version {}", changeSet.name(), changeSet.version());
            }
        } catch (Throwable t) {
            log.error("Database update failed with error: ", t);
            checkExitOnError();
        }
    }

    private boolean validateVersions(List<ChangeSet> changeSets, int currentVersion) {
        for (ChangeSet changeSet : changeSets) {
            if (changeSet.version() != ++currentVersion) {
                return false;
            }
        }
        return true;
    }

    private void checkExitOnError() {
        if (exitOnError) {
            log.error("Stopping application due to database update failure.");
            System.exit(1);
        }
    }

    private List<ChangeSet> getChangeSets(int initVersion) {
        return changeSetProviders.stream()
                .flatMap(ChangeSetProvider::provideChangeSets)
                .filter(changeSet -> changeSet.version() >= initVersion)
                .collect(Collectors.toList());
    }

    private Optional<Integer> getLastChangeSetVersion(List<ChangeSet> changeSets) {
        return changeSets.stream()
                .max(Comparator.comparingInt(ChangeSet::version))
                .map(ChangeSet::version);
    }
}
