package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.StepperMotor;

public interface StepperMotorFactory {
    StepperMotor CreateTheta();
    StepperMotor CreatePhi();
}
