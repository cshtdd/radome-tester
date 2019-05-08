package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.HardwareDaemon;
import com.tddapps.rt.ioc.IocContainer;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.tddapps.rt.test.IocContainerHelper.assertSingleton;

public class IocContainerTest {
    @Test
    public void RegistersDependencies(){
        assertTrue(IocContainer.getInstance().Resolve(PinConverter.class) instanceof PinConverterPi3BPlus);
        assertTrue(IocContainer.getInstance().Resolve(HardwareDaemon.class) instanceof HardwareDaemonEventLoop);
        assertTrue(IocContainer.getInstance().Resolve(StepperMotorFactory.class) instanceof StepperMotorFactorySelector);
        assertTrue(IocContainer.getInstance().Resolve(Delay.class) instanceof Sleep);
        assertTrue(IocContainer.getInstance().Resolve(StepperPrecisionRepository.class) instanceof StepperPrecisionRepositoryInMemory);
        assertTrue(IocContainer.getInstance().Resolve(MovementCalculator.class) instanceof MovementCalculatorDefault);
        assertTrue(IocContainer.getInstance().Resolve(CalibrationService.class) instanceof CalibrationServiceDummy);
        assertTrue(IocContainer.getInstance().Resolve(StepperMotorMover.class) instanceof StepperMotorMoverStatus);
    }

    @Test
    public void RegisterSingletons() {
        assertSingleton(StepperMotorFactoryUln.class);
        assertSingleton(StepperPrecisionRepository.class);
    }

    @Test
    public void StepperMotorFactorySelectorGetsConstructedWithTheTwoCorrectFactories(){
        var factory = (StepperMotorFactorySelector)IocContainer.getInstance().Resolve(StepperMotorFactory.class);

        assertTrue(factory.stepperMotorFactory instanceof StepperMotorFactoryUln);
        assertTrue(factory.stepperMotorFactorySimulator instanceof StepperMotorFactorySimulator);
    }
}
