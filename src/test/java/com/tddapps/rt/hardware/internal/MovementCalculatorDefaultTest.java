package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Direction;
import com.tddapps.rt.model.Position;
import org.junit.Test;

import static org.junit.Assert.*;

public class MovementCalculatorDefaultTest {
    private final MovementCalculator calculator = new MovementCalculatorDefault();

    @Test
    public void ThetaMovesClockwiseWhenPositionsAreEqual() {
        assertEquals(Direction.Clockwise, calculator.CalculateThetaDirection(
                new Position(270, 90),
                new Position(270, 90)
        ));
    }

    @Test
    public void ThetaMovesClockwiseWhenPositionsAreAlmostEqual() {
        assertEquals(Direction.Clockwise, calculator.CalculateThetaDirection(
                new Position(270, 90),
                new Position(270.01, 90)
        ));

        assertEquals(Direction.Clockwise, calculator.CalculateThetaDirection(
                new Position(270, 90),
                new Position(269.99, 90)
        ));

        assertEquals(Direction.Clockwise, calculator.CalculateThetaDirection(
                new Position(270.01, 90),
                new Position(270, 90)
        ));

        assertEquals(Direction.Clockwise, calculator.CalculateThetaDirection(
                new Position(269.99, 90),
                new Position(270, 90)
        ));
    }

    @Test
    public void PhiMovesClockwiseWhenPositionsAreEqual() {
        assertEquals(Direction.Clockwise, calculator.CalculatePhiDirection(
                new Position(270, 90),
                new Position(270, 90)
        ));
    }

    @Test
    public void PhiMovesClockwiseWhenPositionsAreAlmostEqual() {
        assertEquals(Direction.Clockwise, calculator.CalculatePhiDirection(
                new Position(270, 90),
                new Position(270, 90.01)
        ));

        assertEquals(Direction.Clockwise, calculator.CalculatePhiDirection(
                new Position(270, 90),
                new Position(270, 89.99)
        ));

        assertEquals(Direction.Clockwise, calculator.CalculatePhiDirection(
                new Position(270, 90.01),
                new Position(270, 90)
        ));

        assertEquals(Direction.Clockwise, calculator.CalculatePhiDirection(
                new Position(270, 89.99),
                new Position(270, 90)
        ));
    }

    @Test
    public void ThetaMovesCounterClockwiseWhenItsValueIncreases() {
        assertEquals(Direction.CounterClockwise, calculator.CalculateThetaDirection(
                new Position(270, 90),
                new Position(300, 90)
        ));
    }

    @Test
    public void ThetaMovesClockwiseWhenItsValueDecreases() {
        assertEquals(Direction.Clockwise, calculator.CalculateThetaDirection(
                new Position(300, 90),
                new Position(270, 90)
        ));
    }

    @Test
    public void PhiMovesClockwiseWhenItsValueIncreases() {
        assertEquals(Direction.Clockwise, calculator.CalculatePhiDirection(
                new Position(270, 90),
                new Position(270, 120)
        ));
    }

    @Test
    public void PhiMovesCounterClockwiseWhenItsValueDecreases() {
        assertEquals(Direction.CounterClockwise, calculator.CalculatePhiDirection(
                new Position(270, 120),
                new Position(270, 90)
        ));
    }
}
