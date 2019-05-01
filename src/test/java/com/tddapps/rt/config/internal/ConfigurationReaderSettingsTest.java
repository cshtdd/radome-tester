package com.tddapps.rt.config.internal;

import com.tddapps.rt.config.Configuration;
import com.tddapps.rt.config.ConfigurationReader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ConfigurationReaderSettingsTest {
    private final SettingsReader settingsReaderMock = mock(SettingsReader.class);

    private final ConfigurationReader reader = new ConfigurationReaderSettings(
            new ConfigurationReaderJson(), settingsReaderMock
    );

    @Test
    public void ReturnsJsonConfigurationByDefault() {
        var expected = new Configuration();
        expected.setThetaBcmPins(new int[]{1, 2, 3, 4});
        expected.setPhiBcmPins(new int[]{5, 6, 7, 8});

        assertEquals(expected, reader.Read());
    }
}
