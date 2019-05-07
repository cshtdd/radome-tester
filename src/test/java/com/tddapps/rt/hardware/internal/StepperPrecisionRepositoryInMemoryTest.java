package com.tddapps.rt.hardware.internal;

import org.junit.Test;

import static org.junit.Assert.*;


public class StepperPrecisionRepositoryInMemoryTest {
    private final StepperPrecisionRepository repository = new StepperPrecisionRepositoryInMemory();

    @Test
    public void DefaultPrecisionIsNull(){
        assertNull(repository.ReadTheta());
        assertNull(repository.ReadPhi());
    }
}
