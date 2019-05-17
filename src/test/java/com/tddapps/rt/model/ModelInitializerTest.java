package com.tddapps.rt.model;

import com.tddapps.rt.StartupService;
import com.tddapps.rt.test.ConditionAwaiter;
import org.junit.Before;
import org.junit.Test;

import static com.tddapps.rt.test.ConditionAwaiter.await;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class ModelInitializerTest {
    private boolean runWasInvoked = false;
    private final StatusRepository statusRepositoryMock = mock(StatusRepository.class);
    private final PanningDaemon panningDaemonMock = mock(PanningDaemon.class);
    private final StartupService service = new ModelInitializer(statusRepositoryMock, panningDaemonMock);

    private boolean RunWasCalled(){
        return runWasInvoked;
    }

    @Before
    public void setup(){
        doAnswer(i -> {
            runWasInvoked = true;
            return null;
        }).when(panningDaemonMock).run();
    }

    @Test
    public void initializesAStatus(){
        var defaultStatus = Status.builder()
                .isPanning(false)
                .isMoving(false)
                .isCalibrated(false)
                .isHardwareInitialized(false)
                .isHardwareCrash(false)
                .currentPosition(new Position(0, 0))
                .commandedPosition(new Position(0, 0))
                .build();

        service.runAsync(new String[0]);

        verify(statusRepositoryMock).save(defaultStatus);
    }

    @Test
    public void runsThePanningDaemon(){
        service.runAsync(new String[]{});

        assertTrue(ConditionAwaiter.await(this::RunWasCalled));
    }
}
