package com.tddapps.rt.api.controllers;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.model.Position;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
public class MovementController {
    @Data
    public static class MovementRequest {
        private final int thetaDegrees;
        private final int phiDegrees;
    }

    private final IocContainer container;

    public MovementController(IocContainer container) {
        this.container = container;
    }

    @PostMapping(value = "/api/movement")
    ResponseEntity<String> Post(@RequestBody MovementRequest input) {
        log.info(String.format("Movement; input: %s;", input));

        var movementService = container.Resolve(MovementService.class);

        var position = Position.builder()
                .thetaDegrees(input.getThetaDegrees())
                .phiDegrees(input.getPhiDegrees())
                .build();

        if (!movementService.CanMove(position)) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }

        try {
            movementService.Move(position);
        } catch (InvalidOperationException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
