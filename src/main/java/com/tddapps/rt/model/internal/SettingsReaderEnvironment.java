package com.tddapps.rt.model.internal;

import com.tddapps.rt.model.SettingsReader;

public class SettingsReaderEnvironment implements SettingsReader {
    @Override
    public String Read(String name, String defaultValue) {
        return System.getenv()
                .getOrDefault(name, defaultValue);
    }
}
