package com.tddapps.rt.hardware;

import com.google.inject.Inject;
import com.tddapps.rt.StartupService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class HardwareInitializer implements StartupService {
    private final HardwareService hardwareService;

    @Inject
    public HardwareInitializer(HardwareService hardwareService) {
        this.hardwareService = hardwareService;
    }

    @Override
    public void RunAsync(String[] args) {
        log.info("Starting");

        new Thread(hardwareService).start();

        log.info("Finishing");
    }
}
