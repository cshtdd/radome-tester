package com.tddapps.rt.config.internal;

import com.google.inject.Inject;
import com.tddapps.rt.config.Configuration;
import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.config.Settings;
import com.tddapps.rt.utils.Round;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

@Log4j2
class ConfigurationReaderSettings implements ConfigurationReader {
    private final ConfigurationReader configurationReader;
    private final SettingsReader settingsReader;

    @Inject
    public ConfigurationReaderSettings(ConfigurationReaderJson configurationReader, SettingsReader settingsReader) {
        this.configurationReader = configurationReader;
        this.settingsReader = settingsReader;
    }

    @Override
    public Configuration read() {
        var result = readInternal();
        log.info(result);
        return result;
    }

    private Configuration readInternal() {
        var result = configurationReader.read();

        var thetaBcmPinsCsv = settingsReader.read(Settings.THETA_BCM_PINS, "");
        var thetaBcmPinsOverride = csvToIntArray(thetaBcmPinsCsv);
        if (thetaBcmPinsOverride.length > 0){
            result.setThetaBcmPins(thetaBcmPinsOverride);
        }

        var phiBvmPinsCsv = settingsReader.read(Settings.PHI_BCM_PINS, "");
        var phiBcmPinsOverride = csvToIntArray(phiBvmPinsCsv);
        if (phiBcmPinsOverride.length > 0){
            result.setPhiBcmPins(phiBcmPinsOverride);
        }

        var simulationStr = settingsReader.read(Settings.IS_SIMULATION, "").toLowerCase();
        if ("true".equals(simulationStr)){
            result.setSimulation(true);
        }

        if ("false".equals(simulationStr)){
            result.setSimulation(false);
        }

        var panningPrecisionStr = settingsReader.read(Settings.PANNING_PRECISION, "").toLowerCase();
        if (!"".equals(panningPrecisionStr)){
            double panningPrecisionRaw = Double.parseDouble(panningPrecisionStr);
            double panningPrecision = Round.toPrecision(panningPrecisionRaw, 2);
            result.setPanningPrecision(panningPrecision);
        }

        return result;
    }

    private int[] csvToIntArray(String csv) {
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .mapToInt(i -> i)
                .toArray();
    }
}
