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
    public void calibrateThetaStepper(StepperMotor motor) {
        stepperPrecisionRepository.saveTheta(new Precision(1, 0.02));

        statusRepository.update(currentStatus -> {
            var updatedPosition = currentStatus.getCurrentPosition()
                    .toBuilder()
                    .thetaDegrees(270)
                    .build();
            return currentStatus
                    .toBuilder()
                    .currentPosition(updatedPosition)
                    .build();
        });
    }

    @Override
    public void calibratePhiStepper(StepperMotor motor) {
        stepperPrecisionRepository.savePhi(new Precision(1, 0.05));

        statusRepository.update(currentStatus -> {
            var updatedPosition = currentStatus.getCurrentPosition()
                    .toBuilder()
                    .phiDegrees(90)
                    .build();
            return currentStatus
                    .toBuilder()
                    .currentPosition(updatedPosition)
                    .build();
        });
    }
}
