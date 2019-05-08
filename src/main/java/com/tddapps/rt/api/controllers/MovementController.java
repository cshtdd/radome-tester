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
    public static class MovementRequest {
        private double theta;
        private double phi;
    }

    private final IocContainer container;

    public MovementController(IocContainer container) {
        this.container = container;
    }

    @PostMapping(value = "/api/movement")
    ResponseEntity<String> Post(@RequestBody MovementRequest request) {
        var mapper = container.Resolve(Mapper.class);
        var movementService = container.Resolve(MovementService.class);

        log.info(String.format("Movement; input: %s;", request));

        var position = mapper.map(request, Position.class);

        if (!movementService.CanMove(position)) {
            log.info("Cannot Move");
            return new ResponseEntity<>("Cannot Move", HttpStatus.BAD_REQUEST);
        }

        try {
            movementService.Move(position);
        } catch (InvalidOperationException e) {
            log.warn("Movement Error; reason: InvalidOperation; details:", e);
            return new ResponseEntity<>("Movement Error", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Movement Error; reason: Unexpected; details:", e);
            return new ResponseEntity<>("Unexpected Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info(String.format("Movement Started; position: %s", position));
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
