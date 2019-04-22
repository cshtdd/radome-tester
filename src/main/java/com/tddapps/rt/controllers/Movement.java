package com.tddapps.rt.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Movement {
    @RequestMapping("/api/movement")
    String Get() {
        return "Movement World!";
    }
}
