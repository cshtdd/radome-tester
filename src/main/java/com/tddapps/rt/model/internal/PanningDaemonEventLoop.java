package com.tddapps.rt.model.internal;

import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.model.PanningDaemon;
import com.tddapps.rt.model.StatusRepository;

class PanningDaemonEventLoop implements PanningDaemon {
    private final StatusRepository statusRepository;
    private final Delay delay;

    PanningDaemonEventLoop(StatusRepository statusRepository, Delay delay) {
        this.statusRepository = statusRepository;
        this.delay = delay;
    }

    @Override
    public void run() {
        while (RunCondition()){
            // TODO finish this

            delay.Yield();
        }
    }

    // this method is here for testing purposes
    // it cannot be removed
    protected boolean RunCondition() {
        return true;
    }
}
