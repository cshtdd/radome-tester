package com.tddapps.rt.config.internal;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class SettingsReaderEnvironmentTest {
    private final SettingsReader reader = new SettingsReaderEnvironment();

    @Test
    public void readsExistingEnvironmentSetting(){
        var expected = System.getenv("PATH");

        var actual = reader.read("PATH", "");

        assertEquals(expected, actual);
    }

    @Test
    public void returnsDefaultValueWhenSettingNotFound(){
        var actual = reader.read("SUPERCALIFRAGILISTIC", "mary");

        assertEquals("mary", actual);
    }
}
