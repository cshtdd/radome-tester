package com.tddapps.rt.config.internal;

import com.tddapps.rt.config.Configuration;
import com.tddapps.rt.config.ConfigurationReader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ConfigurationReaderJsonTest {
    private final ConfigurationReader reader = new ConfigurationReaderJson();

    @Test
    public void readsTheConfigurationFromTheConfigJsonResource(){
        var expected = new Configuration();
        expected.setSimulation(true);
        expected.setThetaBcmPins(new int[]{1, 2, 3, 4});
        expected.setPhiBcmPins(new int[]{5, 6, 7, 8});
        expected.setPanningPrecision(0.5);

        assertEquals(expected, reader.read());
    }
}
