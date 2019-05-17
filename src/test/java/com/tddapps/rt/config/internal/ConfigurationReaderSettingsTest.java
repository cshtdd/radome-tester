package com.tddapps.rt.config.internal;

import com.tddapps.rt.config.Configuration;
import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.config.Settings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigurationReaderSettingsTest {
    private final SettingsReaderStub settingsReaderStub = new SettingsReaderStub();

    private final ConfigurationReader reader = new ConfigurationReaderSettings(
            new ConfigurationReaderJson(), settingsReaderStub
    );

    @Test
    public void returnsJsonConfigurationByDefault() {
        var expected = new Configuration();
        expected.setSimulation(true);
        expected.setThetaBcmPins(new int[]{1, 2, 3, 4});
        expected.setPhiBcmPins(new int[]{5, 6, 7, 8});
        expected.setPanningPrecision(0.5);

        assertEquals(expected, reader.read());
    }

    @Test
    public void overridesThetaBcmPinsWithSettingsValue(){
        settingsReaderStub.Settings.put(Settings.THETA_BCM_PINS, "11,21, 31, 41 ");

        var expected = new Configuration();
        expected.setSimulation(true);
        expected.setThetaBcmPins(new int[]{11, 21, 31, 41});
        expected.setPhiBcmPins(new int[]{5, 6, 7, 8});
        expected.setPanningPrecision(0.5);

        assertEquals(expected, reader.read());
    }

    @Test
    public void overridesPhiBcmPinsWithSettingsValue(){
        settingsReaderStub.Settings.put(Settings.PHI_BCM_PINS, "55,56, 57, 58 ");

        var expected = new Configuration();
        expected.setSimulation(true);
        expected.setThetaBcmPins(new int[]{1, 2, 3, 4});
        expected.setPhiBcmPins(new int[]{55, 56, 57, 58});
        expected.setPanningPrecision(0.5);

        assertEquals(expected, reader.read());
    }

    @Test
    public void overridesIsSimulationWithSettingsValue(){
        settingsReaderStub.Settings.put(Settings.IS_SIMULATION, "false");

        var expected = new Configuration();
        expected.setSimulation(false);
        expected.setThetaBcmPins(new int[]{1, 2, 3, 4});
        expected.setPhiBcmPins(new int[]{5, 6, 7, 8});
        expected.setPanningPrecision(0.5);

        assertEquals(expected, reader.read());
    }

    @Test
    public void overridesPanningPrecisionWithSettingsValue(){
        settingsReaderStub.Settings.put(Settings.PANNING_PRECISION, "0.75666");

        var expected = new Configuration();
        expected.setSimulation(true);
        expected.setThetaBcmPins(new int[]{1, 2, 3, 4});
        expected.setPhiBcmPins(new int[]{5, 6, 7, 8});
        expected.setPanningPrecision(0.76);

        assertEquals(expected, reader.read());
    }
}
