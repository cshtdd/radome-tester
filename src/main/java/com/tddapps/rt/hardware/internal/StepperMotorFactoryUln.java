package com.tddapps.rt.hardware.internal;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.hardware.StepperMotor;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class StepperMotorFactoryUln implements StepperMotorFactory {
    private final static String THETA = "Theta";
    private final static String PHI = "Phi";

    private final ConfigurationReader configurationReader;

    private final Object criticalSection = new Object();

    private GpioController gpio;
    private StepperMotor motorTheta;
    private StepperMotor motorPhi;


    public StepperMotorFactoryUln(ConfigurationReader configurationReader) {
        this.configurationReader = configurationReader;
    }

    @Override
    public StepperMotor CreateTheta() throws InvalidOperationException {
        var config = configurationReader.Read();
        var name = THETA;

        synchronized (criticalSection){
            var motor = motorTheta;

            ValidateMotorDoesNotAlreadyExist(name, motor);
            InitializeGpio();



            return null;
        }
    }

    @Override
    public StepperMotor CreatePhi() throws InvalidOperationException {
        var config = configurationReader.Read();
        var motorName = PHI;

        synchronized (criticalSection){
            var currentMotor = motorPhi;

            ValidateMotorDoesNotAlreadyExist(motorName, currentMotor);
            InitializeGpio();



            return null;
        }
    }

    private void ValidateMotorDoesNotAlreadyExist(String name, StepperMotor motor) throws InvalidOperationException {
        if (motor != null) {
            log.warn(String.format("Motor already created; motor: %s", name));
            throw new InvalidOperationException(String.format(
                    "%s motor cannot be created twice", name
            ));
        }
    }

    private void InitializeGpio(){
        if (gpio != null) {
            return;
        }

        gpio = GpioFactory.getInstance();
    }
}
