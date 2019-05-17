package com.tddapps.rt.config.internal;

interface SettingsReader {
    String read(String name, String defaultValue);
}
