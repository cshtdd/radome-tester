package com.tddapps.rt.model;

import com.tddapps.rt.StartupService;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ModelInitializerTest {
    private final StatusRepository statusRepositoryMock = mock(StatusRepository.class);
    private final StartupService service = new ModelInitializer(statusRepositoryMock);

    @Test
    public void InitializesAStatus(){
        var defaultStatus = Status.builder()
                .isMoving(false)
                .isCalibrated(true)
                .isHardwareInitialized(false)
                .currentPosition(new Position(270, 90))
                .commandedPosition(new Position(270, 90))
                .build();

        service.RunAsync(new String[0]);

        verify(statusRepositoryMock).Save(defaultStatus);
    }
}
