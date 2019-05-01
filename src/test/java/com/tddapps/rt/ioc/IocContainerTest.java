package com.tddapps.rt.ioc;

import com.tddapps.rt.ProgramStartup;
import com.tddapps.rt.api.ApiInitializer;
import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.config.internal.ConfigurationReaderSettings;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.HardwareInitializer;
import com.tddapps.rt.hardware.HardwareService;
import com.tddapps.rt.hardware.internal.*;
import com.tddapps.rt.hardware.internal.pi.PinConverterPi3BPlus;
import com.tddapps.rt.mapping.Mapper;
import com.tddapps.rt.mapping.internal.AutoMapper;
import com.tddapps.rt.model.ModelInitializer;
import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.config.internal.SettingsReader;
import com.tddapps.rt.model.StatusRepository;
import com.tddapps.rt.model.internal.MovementServiceStatusChanger;
import com.tddapps.rt.config.internal.SettingsReaderEnvironment;
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
        assertTrue(IocContainer.getInstance().Resolve(Mapper.class) instanceof AutoMapper);
        assertTrue(IocContainer.getInstance().Resolve(MovementService.class) instanceof MovementServiceStatusChanger);
        assertTrue(IocContainer.getInstance().Resolve(HardwareService.class) instanceof HardwareServiceStatus);
        assertTrue(IocContainer.getInstance().Resolve(Delay.class) instanceof Sleep);
        assertTrue(IocContainer.getInstance().Resolve(ConfigurationReader.class) instanceof ConfigurationReaderSettings);
        assertTrue(IocContainer.getInstance().Resolve(StepperMotorFactory.class) instanceof StepperMotorFactoryUln);
    }

    @Test
    public void RegisterSingletons(){
        assertSingleton(StatusRepository.class);
        assertSingleton(Mapper.class);
        assertSingleton(StepperMotorFactory.class);
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
