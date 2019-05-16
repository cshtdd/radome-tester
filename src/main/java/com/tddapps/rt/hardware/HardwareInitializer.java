package com.tddapps.rt.hardware;

import com.google.inject.Inject;
import com.tddapps.rt.StartupService;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

@Log4j2
public class HardwareInitializer implements StartupService {
    private final Runnable[] daemons;

    @Inject
    public HardwareInitializer(MovementDaemon movement, PanningDaemon panning) {
        daemons = new Runnable[]{ movement, panning };
    }

    @Override
    public void RunAsync(String[] args) {
        log.info("Starting");

        Arrays.stream(daemons)
                .map(Thread::new)
                .forEach(Thread::start);

        log.info("Finishing");
    }
}
