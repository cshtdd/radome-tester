package com.tddapps.rt.config.internal;

import com.tddapps.rt.config.Configuration;
import com.tddapps.rt.config.ConfigurationReader;

public class ConfigurationReaderSettings implements ConfigurationReader {
    private final ConfigurationReader configurationReader;
    private final SettingsReader settingsReader;

    public ConfigurationReaderSettings(ConfigurationReaderJson configurationReader, SettingsReader settingsReader) {
        this.configurationReader = configurationReader;
        this.settingsReader = settingsReader;
    }

    @Override
    public Configuration Read() {
        return configurationReader.Read();
    }
}
