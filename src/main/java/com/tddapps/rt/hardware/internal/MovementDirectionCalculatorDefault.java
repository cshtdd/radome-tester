package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Direction;
import com.tddapps.rt.hardware.MovementDirectionCalculator;
import com.tddapps.rt.model.Position;

class MovementDirectionCalculatorDefault implements MovementDirectionCalculator {
    @Override
    public Direction CalculateThetaDirection(Position src, Position dest) {
        return null;
    }

    @Override
    public Direction CalculatePhiDirection(Position src, Position dest) {
        return null;
    }
}
