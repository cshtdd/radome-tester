package com.tddapps.rt.model;

import com.tddapps.rt.StartupService;
import org.junit.Before;
import org.junit.Test;

import static com.tddapps.rt.test.ConditionAwaiter.Await;
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
    public void Setup(){
        doAnswer(i -> {
            runWasInvoked = true;
            return null;
        }).when(panningDaemonMock).run();
    }

    @Test
    public void InitializesAStatus(){
        var defaultStatus = Status.builder()
                .isPanning(false)
                .isMoving(false)
                .isCalibrated(false)
                .isHardwareInitialized(false)
                .isHardwareCrash(false)
                .currentPosition(new Position(0, 0))
                .commandedPosition(new Position(0, 0))
                .build();

        service.RunAsync(new String[0]);

        verify(statusRepositoryMock).Save(defaultStatus);
    }

    @Test
    public void RunsThePanningDaemon(){
        service.RunAsync(new String[]{});

        assertTrue(Await(this::RunWasCalled));
    }
}
