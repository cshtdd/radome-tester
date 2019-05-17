package com.tddapps.rt.hardware;

import com.google.inject.Inject;
import com.tddapps.rt.StartupService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class HardwareInitializer implements StartupService {
    private final MovementDaemon daemon;

    @Inject
    public HardwareInitializer(MovementDaemon daemon) {
        this.daemon = daemon;
    }

    @Override
    public void runAsync(String[] args) {
        log.info("Starting");

        new Thread(daemon).start();

        log.info("Finishing");
    }
}
