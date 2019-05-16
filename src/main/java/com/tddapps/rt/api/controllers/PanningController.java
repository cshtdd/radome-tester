package com.tddapps.rt.api.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class PanningController {
    @PostMapping(value = "/api/panning")
    ResponseEntity<String> Post() {
        log.info("Cannot Pan");
        return new ResponseEntity<>("Cannot Pan", HttpStatus.BAD_REQUEST);
    }
}
