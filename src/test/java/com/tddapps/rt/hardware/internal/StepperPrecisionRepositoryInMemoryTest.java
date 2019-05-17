package com.tddapps.rt.hardware.internal;

import org.junit.Test;

import static org.junit.Assert.*;


public class StepperPrecisionRepositoryInMemoryTest {
    private final StepperPrecisionRepository repository = new StepperPrecisionRepositoryInMemory();

    @Test
    public void defaultThetaPrecisionIsNull(){
        assertNull(repository.readTheta());
    }

    @Test
    public void defaultPhiPrecisionIsNull(){
        assertNull(repository.readPhi());
    }

    @Test
    public void savesThetaPrecision(){
        var seededPrecision = new Precision(25, 0.0001);

        repository.saveTheta(seededPrecision);

        assertEquals(seededPrecision, repository.readTheta());
    }

    @Test
    public void savesPhiPrecision(){
        var seededPrecision = new Precision(15, 0.0001);

        repository.savePhi(seededPrecision);

        assertEquals(seededPrecision, repository.readPhi());
    }
}
