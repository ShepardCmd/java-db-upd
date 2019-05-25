package com.shepardcmd.javadbupd;

import java.sql.Connection;

public interface ChangeSet {

    int version();

    String name();

    boolean execute(Connection connection);
}
