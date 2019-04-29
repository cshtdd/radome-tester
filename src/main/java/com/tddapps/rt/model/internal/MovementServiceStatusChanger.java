package com.tddapps.rt.model.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.StatusRepository;

public class MovementServiceStatusChanger implements MovementService {
    private final StatusRepository statusRepository;

    public MovementServiceStatusChanger(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public boolean CanMove(Position position) {
        if (!position.isValid()){
            return false;
        }

        return !statusRepository.CurrentStatus().isMoving();
    }

    @Override
    public void Move(Position position) throws InvalidOperationException {
        if (!CanMove(position)){
            throw new InvalidOperationException("Cannot Move");
        }

        var updatedStatus = statusRepository.CurrentStatus()
                .toBuilder()
                .isMoving(true)
                .commandedPosition(position)
                .build();
        statusRepository.Save(updatedStatus);
    }

    @Override
    public void Stop() {
        var updatedStatus = statusRepository.CurrentStatus()
                .toBuilder()
                .isMoving(false)
                .build();
        statusRepository.Save(updatedStatus);
    }
}
