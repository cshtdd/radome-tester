package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.StepperMotor;

interface CalibrationService {
    void CalibrateThetaStepper(StepperMotor motor);
    void CalibratePhiStepper(StepperMotor motor);
}
