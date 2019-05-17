package com.tddapps.rt.model.internal;

import com.google.inject.Inject;
import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.model.*;

import java.util.ArrayList;
import java.util.stream.IntStream;

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
        var positions = ReadPanningPositions();
        var index = 0;

        while (RunCondition()){
            var status = statusRepository.CurrentStatus();
            var position = positions[index];
            if (status.isPanning()){
                if (movementService.CanMove(position)){
                    try {
                        index++;

                        if (index == positions.length){
                            index = 0;
                            CompletePanning();
                        }

                        movementService.Move(position);
                    } catch (InvalidOperationException e) {

                    }
                }
            }

            delay.Yield();
        }
    }

    private void CompletePanning() {
        statusRepository.Update(currentStatus -> currentStatus
                .toBuilder()
                .isPanning(false)
                .build());
    }

    private Position[] ReadPanningPositions(){
        var settings = settingsRepository.Read();

        var result = new ArrayList<Position>();

        for (double theta = settings.getMinTheta(); theta <= settings.getMaxTheta(); theta += settings.getIncrementTheta()){
            for (double phi = settings.getMinPhi(); phi <= settings.getMaxPhi(); phi += settings.getIncrementPhi()){
                result.add(new Position(theta, phi));
            }
        }

        return result.toArray(new Position[0]);
    }

    // this method is here for testing purposes
    // it cannot be removed
    protected boolean RunCondition() {
        return true;
    }
}
