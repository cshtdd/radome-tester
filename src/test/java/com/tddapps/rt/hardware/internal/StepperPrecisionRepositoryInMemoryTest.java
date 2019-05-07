package com.tddapps.rt.hardware.internal;

import org.junit.Test;

import static org.junit.Assert.*;


public class StepperPrecisionRepositoryInMemoryTest {
    private final StepperPrecisionRepository repository = new StepperPrecisionRepositoryInMemory();

    @Test
    public void DefaultThetaPrecisionIsNull(){
        assertNull(repository.ReadTheta());
    }

    @Test
    public void DefaultPhiPrecisionIsNull(){
        assertNull(repository.ReadPhi());
    }

    @Test
    public void SavesThetaPrecision(){
        var seededPrecision = new Precision(25, 0.0001);

        repository.SaveTheta(seededPrecision);

        assertEquals(seededPrecision, repository.ReadTheta());
    }

    @Test
    public void SavesPhiPrecision(){
        var seededPrecision = new Precision(15, 0.0001);

        repository.SavePhi(seededPrecision);

        assertEquals(seededPrecision, repository.ReadPhi());
    }
}
