package com.shepardcmd.javadbupd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
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
        try (Connection connection = dataSource.getConnection()) {
            int currentVersion = getCurrentDbVersion(connection);
            int version = currentVersion + 1;

            List<AbstractChangeSet> changeSets = getChangeSets(version);
            Optional<Integer> lastVersionOptional = getLastChangeSetVersion(changeSets);
            if (!lastVersionOptional.isPresent()) {
                log.info("Couldn't find any change sets.");
                return;
            }
            int lastVersion = lastVersionOptional.get();
            if (lastVersion >= currentVersion) {
                log.info("Database is already up to date. Current version: {}", currentVersion);
                return;
            }
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
        } catch (SQLException e) {
            log.error("Got SQLException during database update: ", e);
        }
    }

    private void checkExitOnError() {
        if (exitOnError) {
            log.error("Stopping application due to database update failure.");
            System.exit(1);
        }
    }

    private List<AbstractChangeSet> getChangeSets(int initVersion) {
        return changeSetProviders.stream()
                .flatMap(ChangeSetProvider::provideChangeSets)
                .filter(changeSet -> changeSet.getVersion() >= initVersion)
                .collect(Collectors.toList());
    }

    private Optional<Integer> getLastChangeSetVersion(List<AbstractChangeSet> changeSets) {
        return changeSets.stream()
                .max(Comparator.comparingInt(AbstractChangeSet::getVersion))
                .map(AbstractChangeSet::getVersion);
    }

    private int getCurrentDbVersion(Connection connection) throws SQLException {
        final String getDbVersionQuery = "SELECT max(version) FROM " + historyTableName + " WHERE successful = TRUE;";
        ResultSet resultSet = connection.createStatement().executeQuery(getDbVersionQuery);
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }
}
