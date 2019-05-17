package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.StepperMotor;

public interface StepperMotorMover {
    void moveTheta(StepperMotor motor) throws InvalidOperationException;
    void movePhi(StepperMotor motor) throws InvalidOperationException;
}
