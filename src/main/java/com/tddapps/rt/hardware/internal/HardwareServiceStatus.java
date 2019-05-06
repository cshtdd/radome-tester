package com.tddapps.rt.hardware.internal;

import com.google.inject.Inject;
import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.HardwareService;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
class HardwareServiceStatus implements HardwareService {
    private final StatusRepository statusRepository;
    private final Delay delay;
    private final StepperMotorFactory stepperMotorFactory;

    @Inject
    public HardwareServiceStatus(
            StatusRepository statusRepository,
            Delay delay,
            StepperMotorFactory stepperMotorFactory) {
        this.statusRepository = statusRepository;
        this.delay = delay;
        this.stepperMotorFactory = stepperMotorFactory;
    }

    @Override
    public void run() {
        var initialStatus = statusRepository.CurrentStatus();

        if (initialStatus.isHardwareInitialized()){
            //TODO log here
            return;
        }

        try {
            //TODO get reference to the motor
            stepperMotorFactory.CreateTheta();
        } catch (InvalidOperationException e) {
            SetHardwareCrashed(e);
            return;
        }

        try {
            stepperMotorFactory.CreatePhi();
        } catch (InvalidOperationException e) {
            SetHardwareCrashed(e);
            return;
        }

        statusRepository.Save(initialStatus.toBuilder().isHardwareInitialized(true).build());
        //TODO log here

        while (RunCondition()){
            // TODO send single movement steps
            delay.Wait(1);
        }
    }

    private void SetHardwareCrashed(Exception e){
        var status = statusRepository
                .CurrentStatus()
                .toBuilder()
                .isHardwareCrash(true)
                .build();
        statusRepository.Save(status);
        log.error("Hardware Crashed", e);
    }

    protected boolean RunCondition(){
        return true;
    }
}
