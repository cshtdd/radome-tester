package com.tddapps.rt.api.controllers;

import com.tddapps.rt.config.Configuration;
import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.ioc.IocContainer;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class ConfigurationController {
    private final IocContainer container;

    public ConfigurationController(IocContainer container) {
        this.container = container;
    }

    @GetMapping(value = "/api/config", produces = "application/json")
    Configuration Get(){
        var configurationReader = container.Resolve(ConfigurationReader.class);

        return configurationReader.Read();
    }
}
