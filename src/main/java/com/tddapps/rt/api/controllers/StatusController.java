package com.tddapps.rt.api.controllers;

import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.mapping.Mapper;
import com.tddapps.rt.model.StatusRepository;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class StatusController {
    @Data
    public static class StatusResponse {
        private Boolean isCalibrating;
        private Boolean isCalibrated;
        private Boolean isHardwareInitialized;
        private Boolean isHardwareCrash;
        private Boolean isMoving;
        private Boolean isPanning;

        private double theta;
        private double phi;

        private double commandedTheta;
        private double commandedPhi;
    }

    private final IocContainer container;

    public StatusController(IocContainer container) {
        this.container = container;
    }

    @GetMapping(value = "/api/status", produces = "application/json")
    StatusResponse Get() {
        var mapper = container.Resolve(Mapper.class);
        var statusRepository = container.Resolve(StatusRepository.class);

        var currentStatus = statusRepository.CurrentStatus();
        return mapper.map(currentStatus, StatusResponse.class);
    }
}
