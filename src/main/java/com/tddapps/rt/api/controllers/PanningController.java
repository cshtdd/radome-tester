package com.tddapps.rt.api.controllers;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.model.MovementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class PanningController {
    private final IocContainer container;

    public PanningController(IocContainer container) {
        this.container = container;
    }

    @PostMapping(value = "/api/panning")
    ResponseEntity<String> Post() {
        var movementService = container.Resolve(MovementService.class);

        log.info("Panning;");

        if (!movementService.CanPan()){
            log.info("Cannot Pan");
            return new ResponseEntity<>("Cannot Pan", HttpStatus.BAD_REQUEST);
        }

        try {
            movementService.Pan();
        } catch (InvalidOperationException e) {
            log.warn("Panning Error; reason: InvalidOperation; details:", e);
            return new ResponseEntity<>("Panning Error", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Panning Error; reason: Unexpected; details:", e);
            return new ResponseEntity<>("Unexpected Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Panning Started");
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
