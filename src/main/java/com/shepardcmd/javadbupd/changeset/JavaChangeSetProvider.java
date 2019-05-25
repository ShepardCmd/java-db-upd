package com.shepardcmd.javadbupd.changeset;

import com.shepardcmd.javadbupd.ChangeSet;
import com.shepardcmd.javadbupd.ChangeSetProvider;
import com.shepardcmd.javadbupd.exception.JavaChangeSetScanException;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
public class JavaChangeSetProvider implements ChangeSetProvider {
    private final String basePackage;
    private final Map<String, Object> dependencies;

    JavaChangeSetProvider(String basePackage, Map<String, Object> dependencies) {
        this.basePackage = basePackage;
        this.dependencies = dependencies;
    }

    public JavaChangeSetProviderBuilder newBuilder() {
        return new JavaChangeSetProviderBuilder();
    }

    @Override
    public Stream<ChangeSet> provideChangeSets() {
        Reflections reflections = new Reflections(basePackage, new SubTypesScanner(true));
        Set<Class<? extends JavaChangeSet>> changeSetClasses = reflections.getSubTypesOf(JavaChangeSet.class);
        List<ChangeSet> changeSets = new ArrayList<>(changeSetClasses.size());
        changeSetClasses.forEach(changeSetClass -> {
            try {
                JavaChangeSet javaChangeSet = changeSetClass.newInstance();
                changeSets.add(new JavaChangeSetWrapper(javaChangeSet, dependencies));
            } catch (InstantiationException | IllegalAccessException e) {
                String errorMessage = "Failed to instantiate java change set class " +  changeSetClass.getName();
                log.error(errorMessage);
                throw new JavaChangeSetScanException(errorMessage, e);
            }
        });
        return changeSets.stream();
    }
}
