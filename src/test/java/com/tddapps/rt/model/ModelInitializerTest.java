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
                .degreesTheta(270)
                .degreesPhi(90)
                .build();

        service.RunAsync(new String[0]);

        verify(statusRepositoryMock).Save(defaultStatus);
    }
}
