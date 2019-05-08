package com.tddapps.rt.model;

import com.google.inject.Inject;
import com.tddapps.rt.StartupService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ModelInitializer implements StartupService {
    private final StatusRepository statusRepository;

    @Inject
    public ModelInitializer(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public void RunAsync(String[] args) {
        log.info("Starting");

        statusRepository.Save(buildDefaultStatus());
    }

    private Status buildDefaultStatus() {
        var defaultPosition = new Position(0, 0);
        return Status.builder()
                .currentPosition(defaultPosition)
                .commandedPosition(defaultPosition)
                .build();
    }
}
