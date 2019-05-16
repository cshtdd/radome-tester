package com.tddapps.rt.hardware;

import com.tddapps.rt.StartupService;
import org.junit.Before;
import org.junit.Test;

import static com.tddapps.rt.test.ConditionAwaiter.Await;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class HardwareInitializerTest {
    private int runCount = 0;
    private final MovementDaemon movementDaemonMock = mock(MovementDaemon.class);

    private final StartupService service = new HardwareInitializer(movementDaemonMock);

    @Before
    public void Setup(){
        doAnswer(i -> {
            runCount++;
            return null;
        }).when(movementDaemonMock).run();
    }

    @Test
    public void RunsTheMovementDaemons() {
        service.RunAsync(new String[]{});

        assertTrue(Await(() -> runCount == 1));
    }
}
