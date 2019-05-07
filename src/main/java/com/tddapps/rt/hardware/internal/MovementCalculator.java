package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Direction;
import com.tddapps.rt.model.Position;

interface MovementCalculator {
    int CalculateThetaSteps(Position src, Position dest, Precision precision);
    Direction CalculateThetaDirection(Position src, Position dest);

    int CalculatePhiSteps(Position src, Position dest, Precision precision);
    Direction CalculatePhiDirection(Position src, Position dest);
}
