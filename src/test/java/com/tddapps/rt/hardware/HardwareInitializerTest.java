package com.tddapps.rt.hardware;

import com.tddapps.rt.StartupService;
import org.junit.Before;
import org.junit.Test;

import static com.tddapps.rt.test.ConditionAwaiter.Await;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class HardwareInitializerTest {
    private boolean runInvoked = false;
    private final MovementDaemon movementDaemonMock = mock(MovementDaemon.class);

    private final StartupService service = new HardwareInitializer(movementDaemonMock);

    private boolean RunWasInvoked(){
        return runInvoked;
    }

    @Before
    public void Setup(){
        doAnswer(i -> {
            runInvoked = true;
            return null;
        }).when(movementDaemonMock).run();
    }

    @Test
    public void RunsTheHardwareService() {
        service.RunAsync(new String[]{});

        assertTrue(Await(this::RunWasInvoked));
    }
}
