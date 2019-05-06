package com.tddapps.rt.hardware;

import com.tddapps.rt.model.Position;

public interface MovementDirectionCalculator {
    Direction CalculateThetaDirection(Position src, Position dest);
    Direction CalculatePhiDirection(Position src, Position dest);
}
