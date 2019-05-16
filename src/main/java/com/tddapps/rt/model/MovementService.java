package com.tddapps.rt.model;

import com.tddapps.rt.InvalidOperationException;

public interface MovementService {
    boolean CanPan();
    boolean CanMove(Position position);
    void Move(Position position) throws InvalidOperationException;
    void Stop();
}
