package com.tddapps.rt.hardware;

import com.tddapps.rt.StartupService;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class HardwareInitializerTest {
    private boolean runInvoked = false;
    private final HardwareService hardwareServiceMock = mock(HardwareService.class);

    private final StartupService service = new HardwareInitializer(hardwareServiceMock);

    @Before
    public void Setup(){
        doAnswer(i -> {
            runInvoked = true;
            return null;
        }).when(hardwareServiceMock).run();
    }

    @Test
    public void RunsTheHardwareService() throws InterruptedException {
        service.RunAsync(new String[]{});

        for (int i = 0; i < 500; i++) {
            if (runInvoked){
                break;
            }

            Thread.sleep(1);
        }

        assertTrue(runInvoked);
    }
}
