package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.StepperMotor;

interface CalibrationService {
    void calibrateThetaStepper(StepperMotor motor) throws InvalidOperationException;
    void calibratePhiStepper(StepperMotor motor) throws InvalidOperationException;
}
