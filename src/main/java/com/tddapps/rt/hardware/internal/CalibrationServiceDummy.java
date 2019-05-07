package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.StepperMotor;

public class CalibrationServiceDummy implements CalibrationService {
    private final StepperPrecisionRepository stepperPrecisionRepository;

    public CalibrationServiceDummy(StepperPrecisionRepository stepperPrecisionRepository) {
        this.stepperPrecisionRepository = stepperPrecisionRepository;
    }

    @Override
    public void CalibrateThetaStepper(StepperMotor motor) {
        stepperPrecisionRepository.SaveTheta(new Precision(1, 0.02));
    }

    @Override
    public void CalibratePhiStepper(StepperMotor motor) {
        stepperPrecisionRepository.SavePhi(new Precision(1, 0.05));
    }
}
