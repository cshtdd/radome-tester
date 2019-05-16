package com.tddapps.rt.model;

import com.tddapps.rt.InvalidOperationException;

public interface MovementService {
    boolean CanPan();
    boolean CanMove(Position position);
    void Pan() throws InvalidOperationException;
    void Move(Position position) throws InvalidOperationException;
    void Stop();
}
