package com.tddapps.rt;

import lombok.extern.log4j.Log4j2;
import java.util.Arrays;

@Log4j2
public class ProgramStartup {
    public final StartupService[] services;

    public ProgramStartup(StartupService[] services) {
        this.services = services;
    }

    public void Run(String[] args) {
        Arrays.stream(services)
                .forEach(s -> s.RunAsync(args));
    }
}
