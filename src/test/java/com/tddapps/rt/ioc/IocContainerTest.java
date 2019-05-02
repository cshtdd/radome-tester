package com.tddapps.rt.ioc;

import com.tddapps.rt.ProgramStartup;
import com.tddapps.rt.api.ApiInitializer;
import com.tddapps.rt.hardware.HardwareInitializer;
import com.tddapps.rt.model.ModelInitializer;
import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.model.StatusRepository;
import com.tddapps.rt.model.internal.MovementServiceStatusChanger;
import com.tddapps.rt.model.internal.StatusRepositoryInMemory;
import org.junit.Test;

import static com.tddapps.rt.test.IocContainerHelper.assertSingleton;
import static org.junit.Assert.*;

public class IocContainerTest {
    @Test
    public void RegistersDependencies(){
        assertTrue(IocContainer.getInstance().Resolve(StatusRepository.class) instanceof StatusRepositoryInMemory);
        assertTrue(IocContainer.getInstance().Resolve(MovementService.class) instanceof MovementServiceStatusChanger);
    }

    @Test
    public void RegisterSingletons(){
        assertSingleton(StatusRepository.class);
    }

    @Test
    public void RegistersStartupServices(){
        var program = IocContainer.getInstance().Resolve(ProgramStartup.class);

        assertEquals(3, program.services.length);
        assertTrue(program.services[0] instanceof ApiInitializer);
        assertTrue(program.services[1] instanceof ModelInitializer);
        assertTrue(program.services[2] instanceof HardwareInitializer);
    }
}
