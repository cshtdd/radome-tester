package com.tddapps.rt.model.internal;

import com.tddapps.rt.config.Configuration;
import com.tddapps.rt.config.ConfigurationReader;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PanningSettingsRepositoryFixedTest {
    private Configuration configuration = new Configuration();

    private final ConfigurationReader configurationReaderMock = mock(ConfigurationReader.class);
    private final PanningSettingsRepository repository = new PanningSettingsRepositoryFixed(configurationReaderMock);

    @Before
    public void Setup(){
        when(configurationReaderMock.Read()).thenReturn(configuration);
    }

    @Test
    public void ReturnsTheCorrectSettings(){
        var expected = new PanningSettings(
                180, 360, 0.1,
                0, 180, 0.1
        );

        var actual = repository.Read();

        assertEquals(expected, actual);
    }

    @Test
    public void ReturnsTheSimulationSettings(){
        configuration.setSimulation(true);
        var expected = new PanningSettings(
                180, 360, 1,
                0, 180, 1
        );

        var actual = repository.Read();

        assertEquals(expected, actual);
    }
}
