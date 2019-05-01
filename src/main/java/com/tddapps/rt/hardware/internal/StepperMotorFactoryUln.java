package com.tddapps.rt.hardware.internal;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.Pin;
import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.hardware.StepperMotor;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

@Log4j2
public class StepperMotorFactoryUln implements StepperMotorFactory {
    private final static String THETA = "Theta";
    private final static String PHI = "Phi";

    private final ConfigurationReader configurationReader;
    private final PinConverter pinConverter;

    private final Object criticalSection = new Object();

    private GpioController gpio;
    private StepperMotor motorTheta;
    private StepperMotor motorPhi;


    public StepperMotorFactoryUln(ConfigurationReader configurationReader, PinConverter pinConverter) {
        this.configurationReader = configurationReader;
        this.pinConverter = pinConverter;
    }

    @Override
    public StepperMotor CreateTheta() throws InvalidOperationException {
        var name = THETA;
        var bcmPins = configurationReader.Read().getThetaBcmPins();
        ValidateConfigurationPins(name, bcmPins);

        synchronized (criticalSection) {
            var motor = motorTheta;

            ValidateMotorDoesNotAlreadyExist(name, motor);
            InitializeGpio();
            var pins = BuildPins(name, bcmPins);

            
            return null;
        }
    }

    @Override
    public StepperMotor CreatePhi() throws InvalidOperationException {
        var name = PHI;
        var bcmPins = configurationReader.Read().getPhiBcmPins();
        ValidateConfigurationPins(name, bcmPins);

        synchronized (criticalSection) {
            var currentMotor = motorPhi;

            ValidateMotorDoesNotAlreadyExist(name, currentMotor);
            InitializeGpio();
            var pins = BuildPins(name, bcmPins);


            return null;
        }
    }

    private Pin[] BuildPins(String name, int[] bcmPins) throws InvalidOperationException {
        try {
            return Arrays.stream(bcmPins)
                    .mapToObj(pinConverter::BCMToGPIO)
                    .toArray(Pin[]::new);
        } catch (IllegalArgumentException e) {
            log.error(String.format(
                    "Error converting BCM pins; motor: %s; bcmPins: %s;",
                    name, Arrays.toString(bcmPins)
            ), e);
            throw new InvalidOperationException(String.format(
                    "Error converting BCM pins; motor: %s", name
            ), e);
        }
    }

    private void ValidateConfigurationPins(String name, int[] bcmPins) throws InvalidOperationException {
        if (bcmPins == null || bcmPins.length != 4) {
            throw new InvalidOperationException(String.format("Invalid number of bcmPins; motor: %s", name));
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

    private void InitializeGpio() {
        if (gpio != null) {
            return;
        }

        gpio = GpioFactory.getInstance();
    }
}
