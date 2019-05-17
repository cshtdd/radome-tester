package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.StepperMotor;

interface StepperMotorFactory {
    StepperMotor createTheta() throws InvalidOperationException;
    StepperMotor createPhi() throws InvalidOperationException;
}
