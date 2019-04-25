package com.tddapps.rt.model;

import com.tddapps.rt.StartupService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ModelInitializer implements StartupService {
    private final StatusRepository statusRepository;

    public ModelInitializer(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public void RunAsync(String[] args) {
        log.info("Starting");

        statusRepository.Save(buildDefaultStatus());
    }

    private Status buildDefaultStatus() {
        return Status.builder()
                .isMoving(false)
                .degreesTheta(270)
                .degreesPhi(90)
                .build();
    }
}
