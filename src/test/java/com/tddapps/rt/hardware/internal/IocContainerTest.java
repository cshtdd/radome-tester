package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.MovementDaemon;
import com.tddapps.rt.ioc.IocContainer;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.tddapps.rt.test.IocContainerHelper.assertSingleton;

public class IocContainerTest {
    @Test
    public void registersDependencies(){
        assertTrue(IocContainer.getInstance().resolve(PinConverter.class) instanceof PinConverterPi3BPlus);
        assertTrue(IocContainer.getInstance().resolve(MovementDaemon.class) instanceof MovementDaemonEventLoop);
        assertTrue(IocContainer.getInstance().resolve(StepperMotorFactory.class) instanceof StepperMotorFactorySelector);
        assertTrue(IocContainer.getInstance().resolve(Delay.class) instanceof Sleep);
        assertTrue(IocContainer.getInstance().resolve(StepperPrecisionRepository.class) instanceof StepperPrecisionRepositoryInMemory);
        assertTrue(IocContainer.getInstance().resolve(MovementCalculator.class) instanceof MovementCalculatorDefault);
        assertTrue(IocContainer.getInstance().resolve(CalibrationService.class) instanceof CalibrationServiceDummy);
        assertTrue(IocContainer.getInstance().resolve(StepperMotorMover.class) instanceof StepperMotorMoverStatus);
    }

    @Test
    public void registerSingletons() {
        assertSingleton(StepperMotorFactoryUln.class);
        assertSingleton(StepperPrecisionRepository.class);
    }

    @Test
    public void stepperMotorFactorySelectorGetsConstructedWithTheTwoCorrectFactories(){
        var factory = (StepperMotorFactorySelector)IocContainer.getInstance().resolve(StepperMotorFactory.class);

        assertTrue(factory.stepperMotorFactory instanceof StepperMotorFactoryUln);
        assertTrue(factory.stepperMotorFactorySimulator instanceof StepperMotorFactorySimulator);
    }
}
