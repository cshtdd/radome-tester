package com.tddapps.rt.hardware;

import com.tddapps.rt.StartupService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class HardwareInitializer implements StartupService {
    @Override
    public void Run(String[] args) {
        log.info("Starting");
    }
}
