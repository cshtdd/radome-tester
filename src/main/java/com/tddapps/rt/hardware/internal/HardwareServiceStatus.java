package com.tddapps.rt.hardware.internal;

import com.google.inject.Inject;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.HardwareService;
import com.tddapps.rt.model.StatusRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class HardwareServiceStatus implements HardwareService {
    private final StatusRepository statusRepository;
    private final Delay delay;

    @Inject
    public HardwareServiceStatus(StatusRepository statusRepository, Delay delay) {
        this.statusRepository = statusRepository;
        this.delay = delay;
    }

    @Override
    public void run() {
        //TODO finish this

        var initialStatus = statusRepository.CurrentStatus();

        if (initialStatus.isHardwareInitialized()){
            //TODO log here
            return;
        }

        while (RunCondition()){
            // TODO send single movement steps

            delay.Wait(1);
        }
    }

    protected boolean RunCondition(){
        return true;
    }
}
