package com.tddapps.rt.api.controllers;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.mapping.Mapper;
import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.model.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class MovementController {
    @Data
    @AllArgsConstructor
    public static class MovementStartRequest {
        private double theta;
        private double phi;
    }

    private final IocContainer container;

    public MovementController(IocContainer container) {
        this.container = container;
    }

    @PostMapping(value = "/api/movement/start")
    ResponseEntity<String> start(@RequestBody MovementStartRequest request) {
        var mapper = container.resolve(Mapper.class);
        var movementService = container.resolve(MovementService.class);

        log.info(String.format("Movement Start; input: %s;", request));

        var position = mapper.map(request, Position.class);

        if (!movementService.canMove(position)) {
            log.info("Cannot Move");
            return new ResponseEntity<>("Cannot Move", HttpStatus.BAD_REQUEST);
        }

        try {
            movementService.move(position);
        } catch (InvalidOperationException e) {
            log.warn("Movement Start Error; reason: InvalidOperation; details:", e);
            return new ResponseEntity<>("Movement Error", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Movement Start Error; reason: Unexpected; details:", e);
            return new ResponseEntity<>("Unexpected Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info(String.format("Movement Started; position: %s", position));
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @PostMapping(value = "/api/movement/stop")
    ResponseEntity<String> stop() {
        var movementService = container.resolve(MovementService.class);

        log.info("Movement Stop;");
        try {
            movementService.stop();
        } catch (Exception e) {
            log.error("Movement Stop Error; reason: Unexpected; details:", e);
            return new ResponseEntity<>("Unexpected Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Movement Stopped");
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @PostMapping(value = "/api/movement/pan")
    ResponseEntity<String> pan() {
        var movementService = container.resolve(MovementService.class);

        log.info("Panning;");

        if (!movementService.canPan()){
            log.info("Cannot Pan");
            return new ResponseEntity<>("Cannot Pan", HttpStatus.BAD_REQUEST);
        }

        try {
            movementService.pan();
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
