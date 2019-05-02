package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.HardwareService;
import com.tddapps.rt.ioc.IocContainer;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.tddapps.rt.test.IocContainerHelper.assertSingleton;

public class IocContainerTest {
    @Test
    public void RegistersDependencies(){
        assertTrue(IocContainer.getInstance().Resolve(PinConverter.class) instanceof PinConverterPi3BPlus);
        assertTrue(IocContainer.getInstance().Resolve(HardwareService.class) instanceof HardwareServiceStatus);
        assertTrue(IocContainer.getInstance().Resolve(StepperMotorFactory.class) instanceof StepperMotorFactoryUln);
        assertTrue(IocContainer.getInstance().Resolve(Delay.class) instanceof Sleep);
    }

    @Test
    public void RegisterSingletons() {
        assertSingleton(StepperMotorFactory.class);
    }
}
