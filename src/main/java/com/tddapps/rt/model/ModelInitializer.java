package com.tddapps.rt.model;

import com.google.inject.Inject;
import com.tddapps.rt.StartupService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ModelInitializer implements StartupService {
    private final StatusRepository statusRepository;
    private final PanningDaemon daemon;

    @Inject
    public ModelInitializer(StatusRepository statusRepository, PanningDaemon daemon) {
        this.statusRepository = statusRepository;
        this.daemon = daemon;
    }

    @Override
    public void runAsync(String[] args) {
        log.info("Starting");

        statusRepository.save(buildDefaultStatus());

        new Thread(daemon).start();

        log.info("Finishing");
    }

    private Status buildDefaultStatus() {
        var defaultPosition = new Position(0, 0);
        return Status.builder()
                .currentPosition(defaultPosition)
                .commandedPosition(defaultPosition)
                .build();
    }
}
