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
    private final DelaySimulator delay;

    private final Object criticalSection = new Object();

    private GpioController gpio;
    private StepperMotor motorTheta;
    private StepperMotor motorPhi;


    public StepperMotorFactoryUln(
            ConfigurationReader configurationReader,
            PinConverter pinConverter,
            DelaySimulator delay
    ) {
        this.configurationReader = configurationReader;
        this.pinConverter = pinConverter;
        this.delay = delay;
    }

    @Override
    public StepperMotor CreateTheta() throws InvalidOperationException {
        synchronized (criticalSection) {
            ValidateMotorDoesNotAlreadyExist(THETA, motorTheta);

            var pins = BuildPins(THETA, configurationReader.Read().getThetaBcmPins());
            return motorTheta = CreateMotor(THETA, pins);
        }
    }

    @Override
    public StepperMotor CreatePhi() throws InvalidOperationException {
        synchronized (criticalSection) {
            ValidateMotorDoesNotAlreadyExist(PHI, motorPhi);

            var pins = BuildPins(PHI, configurationReader.Read().getPhiBcmPins());
            return motorPhi = CreateMotor(PHI, pins);
        }
    }

    private StepperMotorUln CreateMotor(String name, Pin[] pins) {
        InitializeGpio();
        return new StepperMotorUln(name, gpio, pins);
    }

    private Pin[] BuildPins(String name, int[] bcmPins) throws InvalidOperationException {
        if (bcmPins == null || bcmPins.length != 4) {
            throw new InvalidOperationException(String.format(
                    "Invalid number of bcmPins; motor: %s", name
            ));
        }

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
