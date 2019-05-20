package com.shepardcmd.javadbupd;

import java.util.List;

@FunctionalInterface
public interface ChangeSetProvider {
    List<AbstractChangeSet> provideChangeSets();
}
