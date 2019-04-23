package com.tddapps.rt;

import com.tddapps.rt.internal.StartupServiceExecutor;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.concurrent.Executors;

@Log4j2
public class ProgramStartup {
    public final StartupService[] services;

    public ProgramStartup(StartupService[] services) {
        this.services = services;
    }

    public void Run(String[] args) {
        try {
            log.info("Starting");

            var executorService = Executors.newFixedThreadPool(services.length);

            Arrays.stream(services)
                    .map(s -> new StartupServiceExecutor(s, args))
                    .forEach(executorService::execute);

            while (true){
                log.info(String.format("Status; isShutdown: %s; isTerminated: %s;",
                        executorService.isShutdown(),
                        executorService.isTerminated()
                ));

                Thread.sleep(5000);
            }
        } catch (Exception e) {
            log.error("Unhandled Exception", e);
        }
    }
}
