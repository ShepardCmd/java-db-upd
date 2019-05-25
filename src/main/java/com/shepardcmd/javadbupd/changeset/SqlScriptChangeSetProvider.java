package com.shepardcmd.javadbupd.changeset;

import com.shepardcmd.javadbupd.ChangeSet;
import com.shepardcmd.javadbupd.ChangeSetProvider;
import com.shepardcmd.javadbupd.exception.SqlScriptChangeSetScanException;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
public class SqlScriptChangeSetProvider implements ChangeSetProvider {
    private final boolean useResources;
    private final String scriptDir;

    private static final String RESOURCES_FOLDER = "com.shepardcmd.java-db-upd";
    private static final String FILE_NAME_PATTERN = "db_update_v(.*).sql";

    SqlScriptChangeSetProvider(boolean useResources, String scriptDir) {
        this.useResources = useResources;
        this.scriptDir = scriptDir;
    }

    public SqlScriptChangeSetProviderBuilder newBuilder() {
        return new SqlScriptChangeSetProviderBuilder();
    }

    @Override
    public Stream<ChangeSet> provideChangeSets() {
        List<ChangeSet> changeSets = new ArrayList<>();
        Pattern pattern = Pattern.compile(FILE_NAME_PATTERN);
        if (useResources) {
            Set<String> scriptUrls = new Reflections(RESOURCES_FOLDER, new ResourcesScanner()).getResources(pattern);
            for (String scriptUrl : scriptUrls) {
                changeSets.add(buildChangeSet(scriptUrl, true));
            }
        }
        if (scriptDir != null && !scriptDir.isEmpty()) {
            File dir = new File(scriptDir);
            File[] files = dir.listFiles((d, name) -> pattern.matcher(name).matches());
            if (files != null) {
                for (File file : files) {
                    changeSets.add(buildChangeSet(file.getAbsolutePath(), false));
                }
            }
        }
        return changeSets.stream();
    }

    private SqlScriptChangeSet buildChangeSet(String scriptUrl, boolean fromResources) {
        int versionStartIndex = scriptUrl.lastIndexOf("db_update_v") + 11;
        if (versionStartIndex == 10) {
            String errorMessage = "Change set sql script " + scriptUrl +
                    " has incorrect name! Make sure file name satisfies 'db_update_v<version>.sql', where <version> is positive integer.";
            log.error(errorMessage);
            throw new SqlScriptChangeSetScanException(errorMessage);
        }
        try {
            int version = Integer.parseInt(scriptUrl.substring(versionStartIndex).replace(".sql", ""));
            if (version < 1) {
                String errorMessage = "Change set sql script " + scriptUrl +
                        " has incorrect name! Make sure file name satisfies 'db_update_v<version>.sql', where <version> is positive integer.";
                log.error(errorMessage);
                throw new SqlScriptChangeSetScanException(errorMessage);
            }
            return new SqlScriptChangeSet(scriptUrl, fromResources, version);
        } catch (Throwable t) {
            String errorMessage = "Change set sql script " + scriptUrl +
                    " has incorrect name! Make sure file name satisfies 'db_update_v<version>.sql', where <version> is positive integer.";
            log.error(errorMessage);
            throw new SqlScriptChangeSetScanException(errorMessage, t);
        }
    }
}
