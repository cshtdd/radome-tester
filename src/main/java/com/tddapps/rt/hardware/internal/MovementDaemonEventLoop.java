package com.tddapps.rt.hardware.internal;

import com.google.inject.Inject;
import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.MovementDaemon;
import com.tddapps.rt.hardware.StepperMotor;
import com.tddapps.rt.model.StatusRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
class MovementDaemonEventLoop implements MovementDaemon {
    private final StatusRepository statusRepository;
    private final Delay delay;
    private final StepperMotorFactory stepperMotorFactory;
    private final CalibrationService calibrationService;
    private final StepperMotorMover stepperMotorMover;

    @Inject
    public MovementDaemonEventLoop(
            StatusRepository statusRepository,
            Delay delay,
            StepperMotorFactory stepperMotorFactory,
            CalibrationService calibrationServiceMock,
            StepperMotorMover stepperMotorMover) {
        this.statusRepository = statusRepository;
        this.delay = delay;
        this.stepperMotorFactory = stepperMotorFactory;
        calibrationService = calibrationServiceMock;
        this.stepperMotorMover = stepperMotorMover;
    }

    @Override
    public void run() {
        try {
            runInternal();
        } catch (Exception e) {
            setHardwareCrashed(e);
        }
    }

    private void runInternal() throws InvalidOperationException {
        log.info("Initialization Start");

        if (statusRepository.read().isHardwareInitialized()) {
            log.warn("Hardware already initialized");
            return;
        }

        var motorTheta = createThetaMotor();
        var motorPhi = createPhiMotor();

        try {
            motorTheta.init();
            motorPhi.init();

            beginCalibration();
            calibrationService.calibrateThetaStepper(motorTheta);
            calibrationService.calibratePhiStepper(motorPhi);
            completeCalibration();

            while (runCondition()) {
                stepperMotorMover.moveTheta(motorTheta);
                stepperMotorMover.movePhi(motorPhi);
                delay.yield();
            }
        } finally {
            motorPhi.destroy();
            motorTheta.destroy();
        }
    }

    private StepperMotor createPhiMotor() throws InvalidOperationException {
        log.info("Create Phi Motor");
        return stepperMotorFactory.createPhi();
    }

    private StepperMotor createThetaMotor() throws InvalidOperationException {
        log.info("Create Theta Motor");
        return stepperMotorFactory.createTheta();
    }

    private void beginCalibration() {
        log.info("Start Calibration");
        statusRepository.update(status -> status
            .toBuilder()
            .isCalibrating(true)
            .isCalibrated(false)
            .build());
    }

    private void completeCalibration() {
        log.info("Calibration Completed");

        statusRepository.update(status -> status
                .toBuilder()
                .isCalibrating(false)
                .isCalibrated(true)
                .isHardwareInitialized(true)
                .build());

        log.info("Hardware Initialized");
    }

    private void setHardwareCrashed(Exception e) {
        statusRepository.update(status -> status
                .toBuilder()
                .isHardwareCrash(true)
                .build());

        log.error(e.getMessage(), e);
    }

    // this method is here for testing purposes
    // it cannot be removed
    protected boolean runCondition() {
        return true;
    }
}
