package com.tddapps.rt.hardware.internal;

import com.google.inject.Inject;
import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.HardwareService;
import com.tddapps.rt.hardware.StepperMotor;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
class HardwareServiceStatus implements HardwareService {
    private final StatusRepository statusRepository;
    private final Delay delay;
    private final StepperMotorFactory stepperMotorFactory;
    private final CalibrationService calibrationService;

    @Inject
    public HardwareServiceStatus(
            StatusRepository statusRepository,
            Delay delay,
            StepperMotorFactory stepperMotorFactory,
            CalibrationService calibrationServiceMock) {
        this.statusRepository = statusRepository;
        this.delay = delay;
        this.stepperMotorFactory = stepperMotorFactory;
        calibrationService = calibrationServiceMock;
    }

    @Override
    public void run() {
        try {
            RunInternal();
        }
        catch (Exception e){
            SetHardwareCrashed("Unexpected Error", e);
        }
    }

    private void RunInternal() {
        if (statusRepository.CurrentStatus().isHardwareInitialized()) {
            log.warn("Hardware already initialized");
            return;
        }

        StepperMotor motorTheta;
        StepperMotor motorPhi;

        try {
            motorTheta = stepperMotorFactory.CreateTheta();
        } catch (InvalidOperationException e) {
            SetHardwareCrashed("Theta stepper initialization failed", e);
            return;
        }

        try {
            motorPhi = stepperMotorFactory.CreatePhi();
        } catch (InvalidOperationException e) {
            SetHardwareCrashed("Phi stepper initialization failed", e);
            return;
        }

        BeginCalibration();

        try {
            calibrationService.CalibrateThetaStepper(motorTheta);
        } catch (InvalidOperationException e) {
            SetHardwareCrashed("Theta calibration failed", e);
            return;
        }

        try {
            calibrationService.CalibratePhiStepper(motorPhi);
        } catch (InvalidOperationException e) {
            SetHardwareCrashed("Phi calibration failed", e);
            return;
        }

        CompleteCalibration();

        while (RunCondition()) {
            // TODO send single movement steps
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

    private void SetHardwareCrashed(String message, Exception e){
        var status = statusRepository
                .CurrentStatus()
                .toBuilder()
                .isHardwareCrash(true)
                .build();
        statusRepository.Save(status);
        log.error(message, e);
    }

    // this method is here for testing purposes
    // it cannot be removed
    protected boolean RunCondition(){
        return true;
    }
}
