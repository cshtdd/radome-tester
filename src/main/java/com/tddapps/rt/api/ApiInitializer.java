package com.tddapps.rt.api;

import com.tddapps.rt.Program;
import com.tddapps.rt.StartupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;

@Log4j2
public class ApiInitializer implements StartupService {
    @Override
    public void Run(String[] args) {
        log.info("Starting");
        SpringApplication.run(Program.class, args);
    }
}
