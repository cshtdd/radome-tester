package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Direction;
import com.tddapps.rt.model.Position;

interface MovementCalculator {
    Direction CalculateThetaDirection(Position src, Position dest);
    Direction CalculatePhiDirection(Position src, Position dest);
}
