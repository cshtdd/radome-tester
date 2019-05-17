package com.tddapps.rt.model.internal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PanningSettingsRepositoryFixedTest {
    private final PanningSettingsRepository repository = new PanningSettingsRepositoryFixed();

    @Test
    public void ReturnsTheCorrectSettings(){
        var expected = new PanningSettings(
                180, 360, 0.1,
                0, 180, 0.1
        );

        var actual = repository.Read();

        assertEquals(expected, actual);
    }
}
