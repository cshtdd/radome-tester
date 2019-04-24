package com.tddapps.rt.api.controllers;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
public class MovementController {
    @Data
    public static class MovementRequest {
        private final int thetaDegrees;
        private final int phiDegrees;
    }

    @PostMapping(value = "/api/movement")
    String Post(@RequestBody MovementRequest input){
        log.info(String.format("Movement; input: %s;", input));
        return "";
    }
}
