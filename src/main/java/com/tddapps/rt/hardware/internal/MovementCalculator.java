package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Direction;
import com.tddapps.rt.model.Position;

interface MovementCalculator {
    int calculateThetaSteps(Position src, Position dest, Precision precision);
    Direction calculateThetaDirection(Position src, Position dest);

    int calculatePhiSteps(Position src, Position dest, Precision precision);
    Direction calculatePhiDirection(Position src, Position dest);
}
