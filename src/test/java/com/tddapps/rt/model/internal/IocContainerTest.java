package com.tddapps.rt.model.internal;

import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.model.PanningDaemon;
import com.tddapps.rt.model.StatusRepository;
import org.junit.Test;

import static com.tddapps.rt.test.IocContainerHelper.assertSingleton;
import static org.junit.Assert.assertTrue;

public class IocContainerTest {
    @Test
    public void registersDependencies(){
        assertTrue(IocContainer.getInstance().resolve(StatusRepository.class) instanceof StatusRepositoryInMemory);
        assertTrue(IocContainer.getInstance().resolve(MovementService.class) instanceof MovementServiceStatusChanger);
        assertTrue(IocContainer.getInstance().resolve(PanningDaemon.class) instanceof PanningDaemonEventLoop);
        assertTrue(IocContainer.getInstance().resolve(PanningSettingsRepository.class) instanceof PanningSettingsRepositoryFixed);
    }

    @Test
    public void registerSingletons(){
        assertSingleton(StatusRepository.class);
    }
}
