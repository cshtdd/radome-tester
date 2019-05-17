package com.tddapps.rt.hardware.internal;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.StepperMotor;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

@Log4j2
class StepperMotorUln implements StepperMotor {
    private static final int[][] movementSequence = new int[][]{
            new int []{1, 0, 0, 1},
            new int []{1, 0, 0, 0},
            new int []{1, 1, 0, 0},
            new int []{0, 1, 0, 0},
            new int []{0, 1, 1, 0},
            new int []{0, 0, 1, 0},
            new int []{0, 0, 1, 1},
            new int []{0, 0, 0, 1}
    };
    private static final int stepCount = movementSequence.length;


    private final Object statusCriticalSection = new Object();
    private boolean isInitialized = false;
    private boolean isDestroyed = false;
    private int stepIndex = 0;
    private GpioPinDigitalOutput[] stepPins = null;
    private int pinCount = 0;

    private final String name;
    private final GpioController gpio;
    private final Pin[] pinIds;
    private final Delay delay;

    public StepperMotorUln(String name, GpioController gpio, Pin[] pinIds, Delay delay){
        this.name = name;
        this.gpio = gpio;
        this.pinIds = pinIds;
        this.delay = delay;
    }

    private String toLog(String msg){
        return String.format("%s; stepper: %s;", msg, name);
    }

    @Override
    public void init() {
        synchronized (statusCriticalSection){
            if (isInitialized){
                log.warn(toLog("Already Initialized"));
                return;
            }

            stepIndex = 0;
            stepPins = Arrays.stream(pinIds)
                    .map(p -> {
                        var pin = gpio.provisionDigitalOutputPin(p, p.toString(), PinState.LOW);
                        pin.setShutdownOptions(true, PinState.LOW);
                        pin.low();
                        return pin;
                    })
                    .toArray(GpioPinDigitalOutput[]::new);
            pinCount = stepPins.length;

            isInitialized = true;
            isDestroyed = false;

            log.info(toLog("Initialize"));
        }
    }

    @Override
    public void destroy() {
        synchronized (statusCriticalSection){
            if (isDestroyed){
                log.warn(toLog("Already Destroyed"));
                return;
            }

            stepPins = null;

            isInitialized = false;
            isDestroyed = true;

            log.info(toLog("Destroy"));
        }
    }

    @Override
    public boolean moveCW() throws InvalidOperationException {
        synchronized (statusCriticalSection){
            validateStatusSupportsMovement();

            stepIndex += 1;
            if (stepIndex >= stepCount){
                stepIndex = 0;
            }

            move();
        }
        return true;
    }

    @Override
    public boolean moveCCW() throws InvalidOperationException {
        synchronized (statusCriticalSection){
            validateStatusSupportsMovement();

            stepIndex -= 1;
            if (stepIndex < 0){
                stepIndex = stepCount - 1;
            }

            move();
        }
        return true;
    }

    private void validateStatusSupportsMovement() throws InvalidOperationException {
        if (isDestroyed) {
            throw new InvalidOperationException(toLog("Cannot Move a Destroyed Motor"));
        }

        if (!isInitialized) {
            throw new InvalidOperationException(toLog("Cannot Move a Motor that has not been initialized"));
        }
    }

    private void move(){
        var currentStep = movementSequence[stepIndex];

        for (int i = 0; i < pinCount; i++) {
            if (currentStep[i] == 0){
                stepPins[i].low();
            }
            else {
                log.debug(String.format("[%s] Enable %s", name, stepPins[i].getName()));
                stepPins[i].high();
            }
        }

        delay.waitMs(1);
    }
}
