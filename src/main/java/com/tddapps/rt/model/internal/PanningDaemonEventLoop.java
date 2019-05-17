package com.tddapps.rt.model.internal;

import com.google.inject.Inject;
import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.model.*;

class PanningDaemonEventLoop implements PanningDaemon {
    private final StatusRepository statusRepository;
    private final MovementService movementService;
    private final PanningSettingsRepository settingsRepository;
    private final Delay delay;

    @Inject
    PanningDaemonEventLoop(
            StatusRepository statusRepository,
            MovementService movementService,
            PanningSettingsRepository settingsRepository,
            Delay delay) {
        this.statusRepository = statusRepository;
        this.movementService = movementService;
        this.settingsRepository = settingsRepository;
        this.delay = delay;
    }

    @Override
    public void run() {
        var settings = settingsRepository.Read();
        var start = new Position(settings.getMinTheta(), settings.getMinPhi());

        while (RunCondition()){
            var status = statusRepository.CurrentStatus();
            if (status.isPanning()){
                if (movementService.CanMove(start)){
                    try {
                        movementService.Move(start);
                    } catch (InvalidOperationException e) {

                    }
                }

                // TODO finish this
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
