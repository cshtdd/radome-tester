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
            SetHardwareCrashed(e);
            return;
        }

        try {
            motorPhi = stepperMotorFactory.CreatePhi();
        } catch (InvalidOperationException e) {
            SetHardwareCrashed(e);
            return;
        }

        try {
            calibrationService.CalibrateThetaStepper(motorTheta);
        } catch (InvalidOperationException e) {
            SetHardwareCrashed(e);
            return;
        }

        SetHardwareInitialized();

        while (RunCondition()) {
            // TODO send single movement steps
            delay.Wait(1);
        }
    }

    private void SetHardwareInitialized(){
        var status = statusRepository
                .CurrentStatus()
                .toBuilder()
                .isHardwareInitialized(true)
                .build();
        statusRepository.Save(status);
        log.info("Hardware Initialized");
    }

    private void SetHardwareCrashed(Exception e) {
        SetHardwareCrashed("Hardware Crashed", e);
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
