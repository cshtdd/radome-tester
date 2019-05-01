package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.HardwareService;
import com.tddapps.rt.model.StatusRepository;

import static org.mockito.Mockito.mock;

public class HardwareServiceStatusTest {
    private final StatusRepository statusRepositoryMock = mock(StatusRepository.class);
    private final Delay delay = new DelaySimulator();

    private final HardwareService service = new HardwareServiceStatus(statusRepositoryMock, delay);

    
}
