package com.tddapps.rt.config.internal;

import com.tddapps.rt.config.Configuration;
import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.config.Settings;

import java.util.Arrays;

public class ConfigurationReaderSettings implements ConfigurationReader {
    private final ConfigurationReader configurationReader;
    private final SettingsReader settingsReader;

    public ConfigurationReaderSettings(ConfigurationReaderJson configurationReader, SettingsReader settingsReader) {
        this.configurationReader = configurationReader;
        this.settingsReader = settingsReader;
    }

    @Override
    public Configuration Read() {
        var result = configurationReader.Read();

        var thetaBcmPinsCsv = settingsReader.Read(Settings.THETA_BCM_PINS, "");

        var thetaBcmPinsOverride = CsvToIntArray(thetaBcmPinsCsv);
        if (thetaBcmPinsOverride.length > 0){
            result.setThetaBcmPins(thetaBcmPinsOverride);
        }

        return result;
    }

    private int[] CsvToIntArray(String csv) {
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .mapToInt(i -> i)
                .toArray();
    }
}
