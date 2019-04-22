package com.tddapps.rt.api.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Log4j2
@RestController
public class Movement {
    @RequestMapping("/api/movement")
    String Get() {
        var result = String.format("Hello World! %s", new Date().toInstant().toString());
        log.info(result);
        return result;
    }
}
