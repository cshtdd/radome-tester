package com.tddapps.rt.hardware.internal;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.DelaySimulator;
import com.tddapps.rt.hardware.HardwareDaemon;

public class IocModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DelaySimulator.class);
        bind(Delay.class).to(Sleep.class);
        bind(PinConverter.class).to(PinConverterPi3BPlus.class);
        bind(HardwareDaemon.class).to(HardwareDaemonEventLoop.class);
        bind(MovementCalculator.class).to(MovementCalculatorDefault.class);

        bind(StepperMotorFactorySimulator.class);
        bind(StepperMotorFactoryUln.class).in(Singleton.class);
        bind(StepperPrecisionRepository.class).to(StepperPrecisionRepositoryInMemory.class).in(Singleton.class);

        bind(StepperMotorFactory.class).toProvider(StepperMotorFactoryFactory.class);
        bind(CalibrationService.class).to(CalibrationServiceDummy.class);
        bind(StepperMovementService.class).to(StepperMovementServiceStatus.class);
    }

    private static class StepperMotorFactoryFactory implements Provider<StepperMotorFactory>{
        private final ConfigurationReader configurationReader;
        private final StepperMotorFactoryUln factoryReal;
        private final StepperMotorFactorySimulator factorySimulator;

        @Inject
        private StepperMotorFactoryFactory(
                ConfigurationReader configurationReader,
                StepperMotorFactoryUln factoryReal,
                StepperMotorFactorySimulator factorySimulator) {
            this.configurationReader = configurationReader;
            this.factoryReal = factoryReal;
            this.factorySimulator = factorySimulator;
        }

        @Override
        public StepperMotorFactory get() {
            return new StepperMotorFactorySelector(configurationReader, factoryReal, factorySimulator);
        }
    }
}
