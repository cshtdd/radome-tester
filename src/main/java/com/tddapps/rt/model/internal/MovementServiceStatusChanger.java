package com.tddapps.rt.model.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;

public class MovementServiceStatusChanger implements MovementService {
    private final StatusRepository statusRepository;

    public MovementServiceStatusChanger(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    private Status CurrentStatus() {
        return statusRepository.CurrentStatus();
    }

    @Override
    public boolean CanMove(Position position) {
        if (!CanMoveInternal(position)){
            return false;
        }

        var status = CurrentStatus();

        return CanMoveInternal(status);
    }

    @Override
    public void Move(Position position) throws InvalidOperationException {
        if (!CanMoveInternal(position)){
            throw new InvalidOperationException("Cannot Move to position");
        }

        var status = CurrentStatus();

        if (!CanMoveInternal(status)){
            throw new InvalidOperationException("Cannot Move");
        }

        var updatedStatus = status
                .toBuilder()
                .isMoving(true)
                .commandedPosition(position)
                .build();
        statusRepository.Save(updatedStatus);
    }

    @Override
    public void Stop() {
        var updatedStatus = CurrentStatus()
                .toBuilder()
                .isMoving(false)
                .build();
        statusRepository.Save(updatedStatus);
    }

    private boolean CanMoveInternal(Status status) {
        return !status.isMoving() &&
                !status.isCalibrating() &&
                !status.isHardwareCrash() &&
                status.isCalibrated() &&
                status.isHardwareInitialized();
    }

    private boolean CanMoveInternal(Position position) {
        return position.isValid();
    }
}
