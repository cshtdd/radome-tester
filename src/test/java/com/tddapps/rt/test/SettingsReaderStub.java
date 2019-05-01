package com.tddapps.rt.test;

import com.tddapps.rt.config.internal.SettingsReader;

import java.util.HashMap;
import java.util.Map;

public class SettingsReaderStub implements SettingsReader {
    public final Map<String, String> Settings = new HashMap<>();

    @Override
    public String Read(String name, String defaultValue) {
        return Settings.getOrDefault(name, defaultValue);
    }
}
