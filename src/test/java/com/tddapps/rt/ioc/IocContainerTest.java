package com.tddapps.rt.ioc;

import com.tddapps.rt.ProgramStartup;
import com.tddapps.rt.api.ApiInitializer;
import com.tddapps.rt.hardware.HardwareInitializer;
import com.tddapps.rt.hardware.internal.PinConverter;
import com.tddapps.rt.hardware.internal.pi.PinConverterPi3BPlus;
import com.tddapps.rt.model.SettingsReader;
import com.tddapps.rt.model.StatusRepository;
import com.tddapps.rt.model.internal.SettingsReaderEnvironment;
import com.tddapps.rt.model.internal.StatusRepositoryInMemory;
import org.junit.Test;

import static org.junit.Assert.*;

public class IocContainerTest {

    private static <T> void assertSingleton(Class<T> type){
        var object1 = IocContainer.getInstance().Resolve(type);
        var object2 = IocContainer.getInstance().Resolve(type);

        assertSame(object1, object2);
    }

    @Test
    public void DependenciesAreNotSingletonByDefault(){
        var repository1 = IocContainer.getInstance().Resolve(SettingsReader.class);
        var repository2 = IocContainer.getInstance().Resolve(SettingsReader.class);

        assertNotSame(repository1, repository2);
    }

    @Test
    public void RegistersDependencies(){
        assertTrue(IocContainer.getInstance().Resolve(SettingsReader.class) instanceof SettingsReaderEnvironment);
        assertTrue(IocContainer.getInstance().Resolve(PinConverter.class) instanceof PinConverterPi3BPlus);
        assertTrue(IocContainer.getInstance().Resolve(StatusRepository.class) instanceof StatusRepositoryInMemory);
    }

    @Test
    public void RegisterSingletons(){
        assertSingleton(StatusRepository.class);
    }

    @Test
    public void RegistersStartupServices(){
        var program = IocContainer.getInstance().Resolve(ProgramStartup.class);

        assertEquals(2, program.services.length);
        assertTrue(program.services[0] instanceof ApiInitializer);
        assertTrue(program.services[1] instanceof HardwareInitializer);
    }
}
