package com.tddapps.rt.hardware;

import com.tddapps.rt.StartupService;
import org.junit.Before;
import org.junit.Test;

import static com.tddapps.rt.test.ConditionAwaiter.Await;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class HardwareInitializerTest {
    private boolean runWasInvoked = false;
    private final MovementDaemon movementDaemonMock = mock(MovementDaemon.class);

    private final StartupService service = new HardwareInitializer(movementDaemonMock);

    private boolean RunWasCalled(){
        return runWasInvoked;
    }

    @Before
    public void Setup(){
        doAnswer(i -> {
            runWasInvoked = true;
            return null;
        }).when(movementDaemonMock).run();
    }

    @Test
    public void RunsTheMovementDaemon() {
        service.RunAsync(new String[]{});

        assertTrue(Await(this::RunWasCalled));
    }
}
