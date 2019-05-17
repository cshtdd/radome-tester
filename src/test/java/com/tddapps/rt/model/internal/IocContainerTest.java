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
    public void RegistersDependencies(){
        assertTrue(IocContainer.getInstance().Resolve(StatusRepository.class) instanceof StatusRepositoryInMemory);
        assertTrue(IocContainer.getInstance().Resolve(MovementService.class) instanceof MovementServiceStatusChanger);
        assertTrue(IocContainer.getInstance().Resolve(PanningDaemon.class) instanceof PanningDaemonEventLoop);
        assertTrue(IocContainer.getInstance().Resolve(PanningSettingsRepository.class) instanceof PanningSettingsRepositoryFixed);
    }

    @Test
    public void RegisterSingletons(){
        assertSingleton(StatusRepository.class);
    }
}
