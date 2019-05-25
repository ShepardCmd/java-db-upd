package com.shepardcmd.javadbupd.changeset;

import com.shepardcmd.javadbupd.ChangeSet;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.Connection;

@RequiredArgsConstructor
@ToString
@Slf4j
public class SqlScriptChangeSet implements ChangeSet {
    private final String scriptUrl;
    private final boolean fromResources;
    private final int version;

    @Override
    public int version() {
        return version;
    }

    @Override
    public String name() {
        return scriptUrl;
    }

    @Override
    public boolean execute(Connection connection) {
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setAutoCommit(false);
        scriptRunner.setStopOnError(true);
        if (fromResources) {
            scriptRunner.runScript(new InputStreamReader(getClass().getResourceAsStream(scriptUrl)));
        } else {
            try {
                scriptRunner.runScript(new FileReader(scriptUrl));
            } catch (FileNotFoundException e) {
                log.error("Couldn't find change set file {}", scriptUrl);
                return false;
            }
        }
        return true;
    }
}
