package com.tddapps.rt.api.controllers;

import com.tddapps.rt.model.SettingsReader;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Log4j2
@RestController
public class Movement {
    private final SettingsReader settingsReader;

    public Movement(SettingsReader settingsReader) {
        this.settingsReader = settingsReader;
    }

    @RequestMapping("/api/movement")
    String Get() {
        var result = String.format("Hello World! env: %s; %s",
                settingsReader.Read("app_env", "dev"),
                new Date().toInstant().toString()
        );
        log.info(result);
        return result;
    }
}
