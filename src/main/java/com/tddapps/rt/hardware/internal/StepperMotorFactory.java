package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.StepperMotor;

public interface StepperMotorFactory {
    StepperMotor CreateTheta() throws InvalidOperationException;
    StepperMotor CreatePhi() throws InvalidOperationException;
}
