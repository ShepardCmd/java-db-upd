package com.shepardcmd.javadbupd;

import java.util.stream.Stream;

@FunctionalInterface
public interface ChangeSetProvider {
    Stream<ChangeSet> provideChangeSets();
}
