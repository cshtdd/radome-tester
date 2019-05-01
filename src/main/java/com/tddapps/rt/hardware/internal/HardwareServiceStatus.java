package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.HardwareService;
import com.tddapps.rt.model.StatusRepository;

public class HardwareServiceStatus implements HardwareService {

    private final StatusRepository statusRepository;
    private final Delay delay;

    public HardwareServiceStatus(StatusRepository statusRepository, Delay delay) {
        this.statusRepository = statusRepository;
        this.delay = delay;
    }

    @Override
    public void run() {
        //TODO finish this
    }
}
