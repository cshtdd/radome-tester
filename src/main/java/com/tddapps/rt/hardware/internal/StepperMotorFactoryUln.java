package com.tddapps.rt.hardware.internal;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.hardware.StepperMotor;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class StepperMotorFactoryUln implements StepperMotorFactory {
    private final ConfigurationReader configurationReader;

    private final Object criticalSection = new Object();

    private GpioController gpio;
    private StepperMotor motorTheta;
    private StepperMotor motorPhi;


    public StepperMotorFactoryUln(ConfigurationReader configurationReader) {
        this.configurationReader = configurationReader;
    }

    @Override
    public StepperMotor CreateTheta() {
        synchronized (criticalSection){
            InitializeGpio();

            return null;
        }
    }

    @Override
    public StepperMotor CreatePhi() {
        synchronized (criticalSection){
            InitializeGpio();

            return null;
        }
    }

    private void InitializeGpio(){
        if (gpio != null) {
            return;
        }

        gpio = GpioFactory.getInstance();
    }
}
