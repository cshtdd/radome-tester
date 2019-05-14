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
    private final StepperMotorMover stepperMotorMover;

    @Inject
    public HardwareDaemonEventLoop(
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
            RunInternal();
        } catch (Exception e) {
            SetHardwareCrashed(e);
        }
    }

    private void RunInternal() throws InvalidOperationException {
        log.info("Initialization Start");

        if (statusRepository.CurrentStatus().isHardwareInitialized()) {
            log.warn("Hardware already initialized");
            return;
        }

        var motorTheta = CreateThetaMotor();
        var motorPhi = CreatePhiMotor();

        try {
            motorTheta.Init();
            motorPhi.Init();

            BeginCalibration();
            calibrationService.CalibrateThetaStepper(motorTheta);
            calibrationService.CalibratePhiStepper(motorPhi);
            CompleteCalibration();

            while (RunCondition()) {
                stepperMotorMover.MoveTheta(motorTheta);
                stepperMotorMover.MovePhi(motorPhi);
                delay.Yield();
            }
        } finally {
            motorPhi.Destroy();
            motorTheta.Destroy();
        }
    }

    private StepperMotor CreatePhiMotor() throws InvalidOperationException {
        log.info("Create Phi Motor");
        return stepperMotorFactory.CreatePhi();
    }

    private StepperMotor CreateThetaMotor() throws InvalidOperationException {
        log.info("Create Theta Motor");
        return stepperMotorFactory.CreateTheta();
    }

    private void BeginCalibration() {
        log.info("Start Calibration");
        var status = statusRepository
                .CurrentStatus()
                .toBuilder()
                .isCalibrating(true)
                .isCalibrated(false)
                .build();
        statusRepository.Save(status);
    }

    private void CompleteCalibration() {
        log.info("Calibration Completed");
        var status = statusRepository
                .CurrentStatus()
                .toBuilder()
                .isCalibrating(false)
                .isCalibrated(true)
                .isHardwareInitialized(true)
                .build();
        statusRepository.Save(status);
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
