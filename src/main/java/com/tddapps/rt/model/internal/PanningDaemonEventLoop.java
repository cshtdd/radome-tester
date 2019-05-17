package com.tddapps.rt.model.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.model.*;

class PanningDaemonEventLoop implements PanningDaemon {
    private final StatusRepository statusRepository;
    private final MovementService movementService;
    private final Delay delay;

    PanningDaemonEventLoop(StatusRepository statusRepository, MovementService movementService, Delay delay) {
        this.statusRepository = statusRepository;
        this.movementService = movementService;
        this.delay = delay;
    }

    @Override
    public void run() {
        while (RunCondition()){
            var status = statusRepository.CurrentStatus();
            if (status.isPanning() && !status.isMoving() && !status.isHardwareCrash()){
                // TODO finish this

                try {
                    movementService.Move(new Position(180, 0));
                } catch (InvalidOperationException e) {

                }
            }

            delay.Yield();
        }
    }

    // this method is here for testing purposes
    // it cannot be removed
    protected boolean RunCondition() {
        return true;
    }
}
