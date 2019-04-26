package com.tddapps.rt.api.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
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
        private double degreesTheta;
        private double degreesPhi;
        @JsonProperty("isMoving")
        private boolean isMoving;
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