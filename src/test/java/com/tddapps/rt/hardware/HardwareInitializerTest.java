package com.tddapps.rt.hardware;

import com.tddapps.rt.StartupService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.tddapps.rt.test.ConditionAwaiter.Await;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class HardwareInitializerTest {
    private int runCount = 0;
    private final MovementDaemon movementDaemonMock = mock(MovementDaemon.class);
    private final PanningDaemon panningDaemonMock = mock(PanningDaemon.class);

    private final StartupService service = new HardwareInitializer(movementDaemonMock, panningDaemonMock);

    @Before
    public void Setup(){
        doAnswer(i -> {
            runCount += 1;
            return null;
        }).when(movementDaemonMock).run();

        doAnswer(i -> {
            runCount += 10;
            return null;
        }).when(panningDaemonMock).run();
    }

    @Test
    public void RunsTheMovementDaemons() {
        service.RunAsync(new String[]{});

        assertTrue(Await(() -> runCount == 11));
    }
}
