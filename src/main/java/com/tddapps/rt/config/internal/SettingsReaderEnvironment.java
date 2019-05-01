package com.tddapps.rt.config.internal;

import com.tddapps.rt.config.internal.SettingsReader;

public class SettingsReaderEnvironment implements SettingsReader {
    @Override
    public String Read(String name, String defaultValue) {
        return System.getenv()
                .getOrDefault(name, defaultValue);
    }
}
