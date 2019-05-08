package com.tddapps.rt.hardware.internal;

import com.google.inject.Inject;
import com.tddapps.rt.hardware.StepperMotor;
import com.tddapps.rt.model.StatusRepository;

public class CalibrationServiceDummy implements CalibrationService {
    private final StepperPrecisionRepository stepperPrecisionRepository;
    private final StatusRepository statusRepository;

    @Inject
    public CalibrationServiceDummy(StatusRepository statusRepository, StepperPrecisionRepository stepperPrecisionRepository) {
        this.stepperPrecisionRepository = stepperPrecisionRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public void CalibrateThetaStepper(StepperMotor motor) {
        stepperPrecisionRepository.SaveTheta(new Precision(1, 0.02));

        var currentStatus = statusRepository.CurrentStatus();
        var updatedPosition = currentStatus.getCurrentPosition()
                .toBuilder()
                .thetaDegrees(270)
                .build();
        var updatedStatus = currentStatus
                .toBuilder()
                .currentPosition(updatedPosition)
                .build();
        statusRepository.Save(updatedStatus);
    }

    @Override
    public void CalibratePhiStepper(StepperMotor motor) {
        stepperPrecisionRepository.SavePhi(new Precision(1, 0.05));

        var currentStatus = statusRepository.CurrentStatus();
        var updatedPosition = currentStatus.getCurrentPosition()
                .toBuilder()
                .phiDegrees(90)
                .build();
        var updatedStatus = currentStatus
                .toBuilder()
                .currentPosition(updatedPosition)
                .build();
        statusRepository.Save(updatedStatus);
    }
}
