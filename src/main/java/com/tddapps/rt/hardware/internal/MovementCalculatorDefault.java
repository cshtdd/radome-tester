package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Direction;
import com.tddapps.rt.model.Position;

class MovementCalculatorDefault implements MovementCalculator {
    @Override
    public int CalculateThetaSteps(Position src, Position dest, Precision precision) {
        if (src.AlmostEquals(dest)){
            return 0;
        }

        double delta = Math.abs(dest.getThetaDegrees() - src.getThetaDegrees());
        double intervals = delta / precision.getDegreePrecision();
        double stepCount = precision.getStepCount() * intervals;
        return (int)stepCount;
    }

    @Override
    public Direction CalculateThetaDirection(Position src, Position dest) {
        if (!src.AlmostEquals(dest) &&
                src.getThetaDegrees() < dest.getThetaDegrees()) {
            return Direction.CounterClockwise;
        }

        return Direction.Clockwise;
    }

    @Override
    public int CalculatePhiSteps(Position src, Position dest, Precision precision) {
        return 0;
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
