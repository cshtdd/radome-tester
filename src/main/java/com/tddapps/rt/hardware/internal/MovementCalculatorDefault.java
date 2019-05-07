package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Direction;
import com.tddapps.rt.model.Position;

class MovementCalculatorDefault implements MovementCalculator {
    @Override
    public Direction CalculateThetaDirection(Position src, Position dest) {
        if (!src.AlmostEquals(dest) &&
                src.getThetaDegrees() < dest.getThetaDegrees()) {
            return Direction.CounterClockwise;
        }

        return Direction.Clockwise;
    }

    @Override
    public Direction CalculatePhiDirection(Position src, Position dest) {
        if (!src.AlmostEquals(dest) &&
                src.getPhiDegrees() > dest.getPhiDegrees()) {
            return Direction.CounterClockwise;
        }

        return Direction.Clockwise;
    }
}
