package com.shepardcmd.javadbupd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@RequiredArgsConstructor
@Slf4j
class HistoryWriter {
    private final String historyTableName;
    private final Connection connection;

    int getCurrentDbVersion() throws SQLException {
        final String getDbVersionQuery = "SELECT max(version) FROM " + historyTableName + " WHERE successful = TRUE;";
        ResultSet resultSet = connection.createStatement().executeQuery(getDbVersionQuery);
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    void prepareHistoryTable() throws SQLException {
        final String checkTableExistsQuery = "SELECT EXISTS (" +
                "  SELECT 1 FROM information_schema.tables " +
                "  WHERE table_schema = 'public' " +
                "  AND table_name = ?);";
        PreparedStatement statement = connection.prepareStatement(checkTableExistsQuery);
        statement.setString(1, historyTableName);
        ResultSet rs = statement.executeQuery();
        if (!rs.next() || !rs.getBoolean(1)) {
            log.info("No database history table, creating new table with name {}", historyTableName);
            final String createHistoryTableQuery = "CREATE TABLE " + historyTableName + " (" +
                    "  id SERIAL primary key, " +
                    "  version int not null, " +
                    "  change_set_name text not null, " +
                    "  start_time timestamp not null, " +
                    "  end_time timestamp not null, " +
                    "  successful boolean not null);";
            connection.createStatement().execute(createHistoryTableQuery);
        }
    }

    void saveUpdateResult(UpdateResult updateResult) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO " + historyTableName +
                " (version, change_set_name, start_time, end_time, successful) VALUES (?, ?, ?, ?, ?);");
        ps.setInt(1, updateResult.getVersion());
        ps.setString(2, updateResult.getChangeSetName());
        ps.setTimestamp(3, new Timestamp(updateResult.getStartTime().getTime()));
        ps.setTimestamp(4, new Timestamp(updateResult.getEndTime().getTime()));
        ps.setBoolean(5, updateResult.isSuccessful());
        ps.execute();
    }
}
