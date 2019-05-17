package com.tddapps.rt.model;

import com.tddapps.rt.InvalidOperationException;

public interface MovementService {
    boolean canPan();
    boolean canMove(Position position);
    void pan() throws InvalidOperationException;
    void move(Position position) throws InvalidOperationException;
    void stop();
}
