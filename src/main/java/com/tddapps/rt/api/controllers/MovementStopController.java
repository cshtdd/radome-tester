package com.tddapps.rt.api.controllers;

import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.model.MovementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class MovementStopController {
    private final IocContainer container;

    public MovementStopController(IocContainer container) {
        this.container = container;
    }

    @PostMapping(value = "/api/movement/stop")
    ResponseEntity<String> Post() {
        var movementService = container.Resolve(MovementService.class);

        log.info("Halting;");
        try {
            movementService.Stop();
        } catch (Exception e) {
            log.error("Halting Error; reason: Unexpected; details:", e);
            return new ResponseEntity<>("Unexpected Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Halting Started");
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
