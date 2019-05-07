package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.StepperMotor;

interface CalibrationService {
    void CalibrateThetaStepper(StepperMotor motor) throws InvalidOperationException;
    void CalibratePhiStepper(StepperMotor motor) throws InvalidOperationException;
}
