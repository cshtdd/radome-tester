package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Direction;
import com.tddapps.rt.model.Position;

class MovementCalculatorDefault implements MovementCalculator {
    @Override
    public int CalculateThetaSteps(Position src, Position dest, Precision precision) {
        if (src.AlmostEquals(dest)){
            return 0;
        }

        return CalculateSteps(dest.getThetaDegrees() - src.getThetaDegrees(), precision);
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
        if (src.AlmostEquals(dest)){
            return 0;
        }

        return CalculateSteps(dest.getPhiDegrees() - src.getPhiDegrees(), precision);
    }

    @Override
    public Direction CalculatePhiDirection(Position src, Position dest) {
        if (!src.AlmostEquals(dest) &&
                src.getPhiDegrees() > dest.getPhiDegrees()) {
            return Direction.CounterClockwise;
        }

        return Direction.Clockwise;
    }

    private int CalculateSteps(double delta, Precision precision){
        double intervals = Math.abs(delta) / precision.getDegreePrecision();
        double stepCount = precision.getStepCount() * intervals;
        return (int)stepCount;
    }
}
