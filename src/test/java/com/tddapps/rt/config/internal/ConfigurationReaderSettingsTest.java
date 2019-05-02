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
    public void ReturnsJsonConfigurationByDefault() {
        var expected = new Configuration();
        expected.setSimulation(true);
        expected.setThetaBcmPins(new int[]{1, 2, 3, 4});
        expected.setPhiBcmPins(new int[]{5, 6, 7, 8});

        assertEquals(expected, reader.Read());
    }

    @Test
    public void OverridesThetaBcmPinsWithSettingsValue(){
        settingsReaderStub.Settings.put(Settings.THETA_BCM_PINS, "11,21, 31, 41 ");

        var expected = new Configuration();
        expected.setSimulation(true);
        expected.setThetaBcmPins(new int[]{11, 21, 31, 41});
        expected.setPhiBcmPins(new int[]{5, 6, 7, 8});

        assertEquals(expected, reader.Read());
    }

    @Test
    public void OverridesPhiBcmPinsWithSettingsValue(){
        settingsReaderStub.Settings.put(Settings.PHI_BCM_PINS, "55,56, 57, 58 ");

        var expected = new Configuration();
        expected.setSimulation(true);
        expected.setThetaBcmPins(new int[]{1, 2, 3, 4});
        expected.setPhiBcmPins(new int[]{55, 56, 57, 58});

        assertEquals(expected, reader.Read());
    }

    @Test
    public void OverridesIsSimulationWithSettingsValue(){
        settingsReaderStub.Settings.put(Settings.IS_SIMULATION, "false");

        var expected = new Configuration();
        expected.setSimulation(false);
        expected.setThetaBcmPins(new int[]{1, 2, 3, 4});
        expected.setPhiBcmPins(new int[]{5, 6, 7, 8});

        assertEquals(expected, reader.Read());
    }
}
