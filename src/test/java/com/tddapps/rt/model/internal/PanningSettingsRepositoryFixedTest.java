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
    public void setup(){
        when(configurationReaderMock.read()).thenReturn(configuration);
    }

    @Test
    public void returnsTheCorrectSettings(){
        configuration.setPanningPrecision(0.3);
        var expected = new PanningSettings(
                180, 360, 0.3,
                0, 180, 0.3
        );

        var actual = repository.read();

        assertEquals(expected, actual);
    }

    @Test
    public void returnsTheSimulationSettings(){
        configuration.setSimulation(true);
        var expected = new PanningSettings(
                180, 360, 5,
                0, 180, 5
        );

        var actual = repository.read();

        assertEquals(expected, actual);
    }
}
