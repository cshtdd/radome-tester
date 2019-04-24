package com.tddapps.rt.api.controllers;

import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.model.SettingsReader;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Log4j2
@RestController
public class SampleController {
    private final IocContainer container;

    public SampleController(IocContainer container) {
        this.container = container;
    }

    @GetMapping(value = "/api/sample")
    String Get() {
        var settingsReader = container.Resolve(SettingsReader.class);

        var result = String.format("Hello World! env: %s; %s",
                settingsReader.Read("app_env", "dev"),
                new Date().toInstant().toString()
        );
        log.info(result);
        return result;
    }
}
