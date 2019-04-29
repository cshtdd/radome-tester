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
        if (!position.isValid()){
            return false;
        }

        var status = CurrentStatus();
        return !status.isMoving() && !status.isCalibrating();
    }

    @Override
    public void Move(Position position) throws InvalidOperationException {
        if (!CanMove(position)){
            throw new InvalidOperationException("Cannot Move");
        }

        var updatedStatus = CurrentStatus()
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
}
