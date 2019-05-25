package com.shepardcmd.javadbupd.changeset;

import java.sql.Connection;
import java.util.Map;

public interface JavaChangeSet {

    int version();

    boolean execute(Connection connection, Map<String, Object> dependencies);
}
