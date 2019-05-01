package com.tddapps.rt.config.internal;

import com.tddapps.rt.config.internal.SettingsReader;
import com.tddapps.rt.config.internal.SettingsReaderEnvironment;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class SettingsReaderEnvironmentTest {
    private final SettingsReader reader = new SettingsReaderEnvironment();

    @Test
    public void ReadsExistingEnvironmentSetting(){
        var expected = System.getenv("PATH");

        var actual = reader.Read("PATH", "");

        assertEquals(expected, actual);
    }

    @Test
    public void ReturnsDefaultValueWhenSettingNotFound(){
        var actual = reader.Read("SUPERCALIFRAGILISTIC", "mary");

        assertEquals("mary", actual);
    }
}