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

    private Status currentStatus() {
        return statusRepository.read();
    }

    @Override
    public boolean canPan() {
        return currentStatus().canPan();
    }

    @Override
    public boolean canMove(Position position) {
        if (!position.isValid()){
            return false;
        }

        return currentStatus().canMove();
    }

    @Override
    public void pan() throws InvalidOperationException{
        if (!canPan()){
            throw new InvalidOperationException("Cannot Pan");
        }

        statusRepository.update(status -> status
                .toBuilder()
                .isPanning(true)
                .build());
    }

    @Override
    public void move(Position position) throws InvalidOperationException {
        if (!position.isValid()){
            throw new InvalidOperationException("Cannot Move to position");
        }

        if (!currentStatus().canMove()){
            throw new InvalidOperationException("Cannot Move");
        }

        statusRepository.update(status -> status
                .toBuilder()
                .isMoving(true)
                .commandedPosition(position)
                .build());
    }

    @Override
    public void stop() {
        statusRepository.update(status -> status
                .toBuilder()
                .isPanning(false)
                .isMoving(false)
                .build());
    }
}
