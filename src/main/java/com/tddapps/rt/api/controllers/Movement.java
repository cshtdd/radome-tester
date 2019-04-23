package com.tddapps.rt.api.controllers;

import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.model.SettingsReader;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Log4j2
@RestController
public class Movement {
    private final IocContainer container;

    public Movement(IocContainer container) {
        this.container = container;
    }

    @RequestMapping("/api/movement")
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
