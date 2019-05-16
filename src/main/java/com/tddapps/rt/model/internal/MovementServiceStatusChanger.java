package com.tddapps.rt.model.internal;

import com.google.inject.Inject;
import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;

class MovementServiceStatusChanger implements MovementService {
    private final StatusRepository statusRepository;

    @Inject
    public MovementServiceStatusChanger(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    private Status CurrentStatus() {
        return statusRepository.CurrentStatus();
    }

    @Override
    public boolean CanPan() {
        var status = CurrentStatus();

        return CanMove(status);
    }

    @Override
    public boolean CanMove(Position position) {
        if (!position.IsValid()){
            return false;
        }

        return CanPan();
    }

    @Override
    public void Move(Position position) throws InvalidOperationException {
        if (!position.IsValid()){
            throw new InvalidOperationException("Cannot Move to position");
        }

        var status = CurrentStatus();

        if (!CanMove(status)){
            throw new InvalidOperationException("Cannot Move");
        }

        statusRepository.Update(currentStatus -> currentStatus
                .toBuilder()
                .isMoving(true)
                .commandedPosition(position)
                .build());
    }

    @Override
    public void Stop() {
        statusRepository.Update(currentStatus -> currentStatus
                .toBuilder()
                .isPanning(false)
                .isMoving(false)
                .build());
    }

    private boolean CanMove(Status status) {
        return !status.isPanning() &&
                !status.isMoving() &&
                !status.isCalibrating() &&
                !status.isHardwareCrash() &&
                status.isCalibrated() &&
                status.isHardwareInitialized();
    }
}
