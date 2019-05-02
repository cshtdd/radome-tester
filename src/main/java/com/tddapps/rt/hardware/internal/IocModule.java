package com.tddapps.rt.hardware.internal;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.DelaySimulator;
import com.tddapps.rt.hardware.HardwareService;

public class IocModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DelaySimulator.class);
        bind(Delay.class).to(Sleep.class);
        bind(PinConverter.class).to(PinConverterPi3BPlus.class);
        bind(HardwareService.class).to(HardwareServiceStatus.class);
        bind(StepperMotorFactory.class).to(StepperMotorFactoryUln.class).in(Singleton.class);
    }
}
