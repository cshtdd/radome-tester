package com.tddapps.rt.config.internal;

import com.tddapps.rt.config.ConfigurationReader;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class ConfigurationReaderJsonTest {
    private final ConfigurationReader reader = new ConfigurationReaderJson();

    @Test
    public void ReadsTheConfigurationFromTheConfigJsonResource(){
        var config = reader.Read();

        assertArrayEquals(new int[]{1, 2, 3, 4}, config.getThetaBcmPins());
        assertArrayEquals(new int[]{5, 6, 7, 8}, config.getPhiBcmPins());
    }
}
