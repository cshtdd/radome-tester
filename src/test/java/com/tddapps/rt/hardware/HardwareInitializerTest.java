package com.tddapps.rt.hardware;

import com.tddapps.rt.StartupService;
import com.tddapps.rt.test.ConditionAwaiter;
import org.junit.Before;
import org.junit.Test;

import static com.tddapps.rt.test.ConditionAwaiter.await;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class HardwareInitializerTest {
    private boolean runWasInvoked = false;
    private final MovementDaemon movementDaemonMock = mock(MovementDaemon.class);

    private final StartupService service = new HardwareInitializer(movementDaemonMock);

    private boolean runWasCalled(){
        return runWasInvoked;
    }

    @Before
    public void setup(){
        doAnswer(i -> {
            runWasInvoked = true;
            return null;
        }).when(movementDaemonMock).run();
    }

    @Test
    public void runsTheMovementDaemon() {
        service.runAsync(new String[]{});

        assertTrue(ConditionAwaiter.await(this::runWasCalled));
    }
}
