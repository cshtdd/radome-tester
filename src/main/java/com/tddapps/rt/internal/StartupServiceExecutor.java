package com.tddapps.rt.internal;

import com.tddapps.rt.StartupService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class StartupServiceExecutor implements Runnable{
    private final StartupService service;
    private final String[] args;

    public StartupServiceExecutor(StartupService service, String[] args) {
        this.service = service;
        this.args = args;
    }

    @Override
    public void run() {
        try{
            service.Run(args);
        }
        catch (Exception e){
            log.error("Thread was interrupted", e);
        }
    }
}
