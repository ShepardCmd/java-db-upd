package com.shepardcmd.javadbupd.changeset;

import java.util.HashMap;
import java.util.Map;

public class JavaChangeSetProviderBuilder {
    private String basePackage;
    private Map<String, Object> dependencies = new HashMap<>();

    public JavaChangeSetProviderBuilder setBasePackage(String basePackage) {
        this.basePackage = basePackage;
        return this;
    }

    public void addDependency(String key, Object dependency) {
        if (dependencies.containsKey(key)) {
            throw new IllegalArgumentException("JavaClassChangeSetProvider dependencies map already contains key " + key);
        }
        dependencies.put(key, dependency);
    }

    public JavaChangeSetProvider build() {
        return new JavaChangeSetProvider(basePackage, new HashMap<>(dependencies));
    }
}
