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
    private List<String> runDaemons = new ArrayList<>();
    private final MovementDaemon movementDaemonMock = mock(MovementDaemon.class);

    private final StartupService service = new HardwareInitializer(movementDaemonMock);

    private boolean RunMovementWasInvoked(){
        return runDaemons.contains("Movement");
    }

    @Before
    public void Setup(){
        doAnswer(i -> {
            runDaemons.add("Movement");
            return null;
        }).when(movementDaemonMock).run();
    }

    @Test
    public void RunsTheHardwareService() {
        service.RunAsync(new String[]{});

        assertTrue(Await(this::RunMovementWasInvoked));
    }
}
