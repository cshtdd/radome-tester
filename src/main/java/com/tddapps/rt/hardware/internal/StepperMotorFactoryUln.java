package com.tddapps.rt.hardware.internal;

import com.google.inject.Inject;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.Pin;
import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.StepperMotor;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

@Log4j2
class StepperMotorFactoryUln implements StepperMotorFactory {
    private final static String THETA = "Theta";
    private final static String PHI = "Phi";

    private final ConfigurationReader configurationReader;
    private final PinConverter pinConverter;
    private final Delay delay;

    private final Object criticalSection = new Object();

    private GpioController gpio;
    private StepperMotor motorTheta;
    private StepperMotor motorPhi;

    @Inject
    public StepperMotorFactoryUln(
            ConfigurationReader configurationReader,
            PinConverter pinConverter,
            Delay delay
    ) {
        this.configurationReader = configurationReader;
        this.pinConverter = pinConverter;
        this.delay = delay;
    }

    @Override
    public StepperMotor createTheta() throws InvalidOperationException {
        synchronized (criticalSection) {
            validateMotorDoesNotAlreadyExist(THETA, motorTheta);

            var pins = buildPins(THETA, configurationReader.read().getThetaBcmPins());
            return motorTheta = createMotor(THETA, pins);
        }
    }

    @Override
    public StepperMotor createPhi() throws InvalidOperationException {
        synchronized (criticalSection) {
            validateMotorDoesNotAlreadyExist(PHI, motorPhi);

            var pins = buildPins(PHI, configurationReader.read().getPhiBcmPins());
            return motorPhi = createMotor(PHI, pins);
        }
    }

    private StepperMotorUln createMotor(String name, Pin[] pins) {
        initializeGpio();
        return new StepperMotorUln(name, gpio, pins, delay);
    }

    private Pin[] buildPins(String name, int[] bcmPins) throws InvalidOperationException {
        if (bcmPins == null || bcmPins.length != 4) {
            throw new InvalidOperationException(String.format(
                    "Invalid number of bcmPins; motor: %s", name
            ));
        }

        try {
            return Arrays.stream(bcmPins)
                    .mapToObj(pinConverter::fromBCMToGPIO)
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

    private void validateMotorDoesNotAlreadyExist(String name, StepperMotor motor) throws InvalidOperationException {
        if (motor != null) {
            log.warn(String.format("Motor already created; motor: %s", name));
            throw new InvalidOperationException(String.format(
                    "%s motor cannot be created twice", name
            ));
        }
    }

    private void initializeGpio() {
        if (gpio != null) {
            return;
        }

        gpio = GpioFactory.getInstance();
    }
}
