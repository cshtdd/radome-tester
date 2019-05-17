package com.tddapps.rt.model.internal;

import com.google.inject.Inject;
import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.model.*;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
class PanningDaemonEventLoop implements PanningDaemon {
    private final StatusRepository statusRepository;
    private final MovementService movementService;
    private final PanningSettingsRepository settingsRepository;
    private final Delay delay;

    private PanningSettings settings;
    private Position[] allPositions;
    private int currentPositionIndex;

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
        try {
            runInternal();
        } catch (Exception e) {
            setHardwareCrashed(e);
        }
    }

    private void setHardwareCrashed(Exception e) {
        statusRepository.update(status -> status
                .toBuilder()
                .isHardwareCrash(true)
                .build());

        log.error(e.getMessage(), e);
    }

    private void runInternal() throws InvalidOperationException {
        initializeInternalState();

        while (runCondition()){
            var status = statusRepository.read();
            prepareInternalState(status);
            if (shouldMove(status)){
                move();
            }
            delay.yield();
        }
    }

    private boolean shouldMove(Status status) {
        return status.isPanning();
    }

    private void move() throws InvalidOperationException {
        var position = readCurrentPosition();
        if (!movementService.canMove(position)) {
            return;
        }

        logMovement(position);
        movementService.move(position);

        incrementPositionIndex();
    }

    private void initializeInternalState() {
        log.info("Initialization Start");

        settings = settingsRepository.read();
        currentPositionIndex = 0;
        allPositions = readPanningPositions();

        log.info("Panning Initialized");
    }

    private void logMovement(Position position) {
        log.info(String.format(
                "Move; current: %d; total: %d; theta: %.2f; phi: %.2f;",
                currentPositionIndex,
                allPositions.length,
                position.getThetaDegrees(),
                position.getPhiDegrees()
        ));
    }

    private Position readCurrentPosition() {
        return allPositions[currentPositionIndex];
    }

    private void incrementPositionIndex() {
        currentPositionIndex++;

        if (currentPositionIndex == allPositions.length){
            currentPositionIndex = 0;
            completePanning();
        }
    }

    private void prepareInternalState(Status status) {
        if (!status.isPanning()){
            currentPositionIndex = 0;
        }
    }

    private void completePanning() {
        statusRepository.update(status -> status
                .toBuilder()
                .isPanning(false)
                .build());
    }

    private Position[] readPanningPositions(){
        return IntStream.rangeClosed(0, thetaStepsCount())
                .mapToDouble(this::stepToTheta)
                .mapToObj(theta -> IntStream.rangeClosed(0, phiStepsCount())
                        .mapToDouble(this::stepToPhi)
                        .mapToObj(phi -> new Position(theta, phi))
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .toArray(Position[]::new);
    }

    private int phiStepsCount() {
        return (int)((settings.getMaxPhi() - settings.getMinPhi()) / settings.getIncrementPhi());
    }

    private int thetaStepsCount() {
        return (int)((settings.getMaxTheta() - settings.getMinTheta()) / settings.getIncrementTheta());
    }

    private double stepToTheta(int i){
        return (i * settings.getIncrementTheta()) + settings.getMinTheta();
    }

    private double stepToPhi(int i){
        return (i * settings.getIncrementPhi()) + settings.getMinPhi();
    }

    // this method is here for testing purposes
    // it cannot be removed
    protected boolean runCondition() {
        return true;
    }
}
