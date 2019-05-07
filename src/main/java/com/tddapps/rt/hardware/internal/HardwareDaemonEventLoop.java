package com.tddapps.rt.hardware.internal;

import com.google.inject.Inject;
import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.HardwareDaemon;
import com.tddapps.rt.hardware.StepperMotor;
import com.tddapps.rt.model.StatusRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
class HardwareDaemonEventLoop implements HardwareDaemon {
    private final StatusRepository statusRepository;
    private final Delay delay;
    private final StepperMotorFactory stepperMotorFactory;
    private final CalibrationService calibrationService;
    private final StepperMovementService stepperMovementService;

    @Inject
    public HardwareDaemonEventLoop(
            StatusRepository statusRepository,
            Delay delay,
            StepperMotorFactory stepperMotorFactory,
            CalibrationService calibrationServiceMock,
            StepperMovementService stepperMovementService) {
        this.statusRepository = statusRepository;
        this.delay = delay;
        this.stepperMotorFactory = stepperMotorFactory;
        calibrationService = calibrationServiceMock;
        this.stepperMovementService = stepperMovementService;
    }

    @Override
    public void run() {
        try {
            RunInternal();
        } catch (Exception e) {
            SetHardwareCrashed(e);
        }
    }

    private void RunInternal() throws InvalidOperationException {
        if (statusRepository.CurrentStatus().isHardwareInitialized()) {
            log.warn("Hardware already initialized");
            return;
        }

        StepperMotor motorTheta;
        StepperMotor motorPhi;

        motorTheta = stepperMotorFactory.CreateTheta();
        motorPhi = stepperMotorFactory.CreatePhi();

        BeginCalibration();
        calibrationService.CalibrateThetaStepper(motorTheta);
        calibrationService.CalibratePhiStepper(motorPhi);
        CompleteCalibration();

        while (RunCondition()) {
            stepperMovementService.MoveTheta(motorTheta);
            stepperMovementService.MovePhi(motorPhi);

            delay.Wait(1);
        }
    }

    private void BeginCalibration() {
        var status = statusRepository
                .CurrentStatus()
                .toBuilder()
                .isCalibrating(true)
                .isCalibrated(false)
                .build();
        statusRepository.Save(status);
        log.info("Starting Calibration");
    }

    private void CompleteCalibration() {
        var status = statusRepository
                .CurrentStatus()
                .toBuilder()
                .isCalibrating(false)
                .isCalibrated(true)
                .isHardwareInitialized(true)
                .build();
        statusRepository.Save(status);
        log.info("Calibration Completed");
        log.info("Hardware Initialized");
    }

    private void SetHardwareCrashed(Exception e) {
        var status = statusRepository
                .CurrentStatus()
                .toBuilder()
                .isHardwareCrash(true)
                .build();
        statusRepository.Save(status);
        log.error(e.getMessage(), e);
    }

    // this method is here for testing purposes
    // it cannot be removed
    protected boolean RunCondition() {
        return true;
    }
}
