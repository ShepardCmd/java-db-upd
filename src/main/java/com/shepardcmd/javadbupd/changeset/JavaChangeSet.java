package com.shepardcmd.javadbupd.changeset;

import java.util.Map;

public interface JavaChangeSet {

    int version();

    void execute(Map<String, Object> dependencies);
}
