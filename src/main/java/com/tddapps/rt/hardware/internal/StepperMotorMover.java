package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.StepperMotor;

public interface StepperMotorMover {
    void MoveTheta(StepperMotor motor) throws InvalidOperationException;
    void MovePhi(StepperMotor motor) throws InvalidOperationException;
}
