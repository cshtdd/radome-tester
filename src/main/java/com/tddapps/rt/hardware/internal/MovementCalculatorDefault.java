package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Direction;
import com.tddapps.rt.model.Position;

class MovementCalculatorDefault implements MovementCalculator {
    @Override
    public int calculateThetaSteps(Position src, Position dest, Precision precision) {
        if (src.almostEquals(dest)){
            return 0;
        }

        return calculateSteps(dest.getThetaDegrees() - src.getThetaDegrees(), precision);
    }

    @Override
    public Direction calculateThetaDirection(Position src, Position dest) {
        if (!src.almostEquals(dest) &&
                src.getThetaDegrees() < dest.getThetaDegrees()) {
            return Direction.CounterClockwise;
        }

        return Direction.Clockwise;
    }

    @Override
    public int calculatePhiSteps(Position src, Position dest, Precision precision) {
        if (src.almostEquals(dest)){
            return 0;
        }

        return calculateSteps(dest.getPhiDegrees() - src.getPhiDegrees(), precision);
    }

    @Override
    public Direction calculatePhiDirection(Position src, Position dest) {
        if (!src.almostEquals(dest) &&
                src.getPhiDegrees() > dest.getPhiDegrees()) {
            return Direction.CounterClockwise;
        }

        return Direction.Clockwise;
    }

    private int calculateSteps(double delta, Precision precision){
        double intervals = Math.abs(delta) / precision.getDegreePrecision();
        double stepCount = precision.getStepCount() * intervals;
        return (int)stepCount;
    }
}
