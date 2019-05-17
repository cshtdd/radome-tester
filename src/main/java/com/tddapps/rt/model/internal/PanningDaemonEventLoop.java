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
            RunInternal();
        } catch (Exception e) {
            SetHardwareCrashed(e);
        }
    }

    private void SetHardwareCrashed(Exception e) {
        statusRepository.Update(currentStatus -> currentStatus
                .toBuilder()
                .isHardwareCrash(true)
                .build());

        log.error(e.getMessage(), e);
    }

    private void RunInternal() throws InvalidOperationException {
        InitializeInternalState();

        while (RunCondition()){
            var status = statusRepository.CurrentStatus();
            PrepareInternalState(status);
            if (ShouldMove(status)){
                Move();
            }
            delay.Yield();
        }
    }

    private boolean ShouldMove(Status status) {
        return status.isPanning();
    }

    private void Move() throws InvalidOperationException {
        var position = ReadCurrentPosition();
        if (!movementService.CanMove(position)) {
            return;
        }

        LogMovement(position);
        movementService.Move(position);

        IncrementPositionIndex();
    }

    private void InitializeInternalState() {
        log.info("Initialization Start");

        settings = settingsRepository.Read();
        currentPositionIndex = 0;
        allPositions = ReadPanningPositions();
    }

    private void LogMovement(Position position) {
        log.info(String.format(
                "Move; stepIndex: %d; count: %d; position: %s",
                currentPositionIndex,
                allPositions.length,
                position
        ));
    }

    private Position ReadCurrentPosition() {
        return allPositions[currentPositionIndex];
    }

    private void IncrementPositionIndex() {
        currentPositionIndex++;

        if (currentPositionIndex == allPositions.length){
            currentPositionIndex = 0;
            CompletePanning();
        }
    }

    private void PrepareInternalState(Status status) {
        if (!status.isPanning()){
            currentPositionIndex = 0;
        }
    }

    private void CompletePanning() {
        statusRepository.Update(currentStatus -> currentStatus
                .toBuilder()
                .isPanning(false)
                .build());
    }

    private Position[] ReadPanningPositions(){
        return IntStream.rangeClosed(0, ThetaStepsCount())
                .mapToDouble(this::StepToTheta)
                .mapToObj(theta -> IntStream.rangeClosed(0, PhiStepsCount())
                        .mapToDouble(this::StepToPhi)
                        .mapToObj(phi -> new Position(theta, phi))
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .toArray(Position[]::new);
    }

    private int PhiStepsCount() {
        return (int)((settings.getMaxPhi() - settings.getMinPhi()) / settings.getIncrementPhi());
    }

    private int ThetaStepsCount() {
        return (int)((settings.getMaxTheta() - settings.getMinTheta()) / settings.getIncrementTheta());
    }

    private double StepToTheta(int i){
        return (i * settings.getIncrementTheta()) + settings.getMinTheta();
    }

    private double StepToPhi(int i){
        return (i * settings.getIncrementPhi()) + settings.getMinPhi();
    }

    // this method is here for testing purposes
    // it cannot be removed
    protected boolean RunCondition() {
        return true;
    }
}
