package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Direction;
import com.tddapps.rt.hardware.MovementDirectionCalculator;
import com.tddapps.rt.model.Position;
import org.junit.Test;

import static org.junit.Assert.*;

public class MovementDirectionCalculatorDefaultTest {
    private final MovementDirectionCalculator calculator = new MovementDirectionCalculatorDefault();

    @Test
    public void ThetaIsMovedClockwiseWhenPositionsAreEqual() {
        assertEquals(Direction.Clockwise, calculator.CalculateThetaDirection(
                new Position(270, 90),
                new Position(270, 90)
        ));
    }

    @Test
    public void ThetaIsMovedClockwiseWhenPositionsAreAlmostEqual() {
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
    public void PhiIsMovedClockwiseWhenPositionsAreEqual() {
        assertEquals(Direction.Clockwise, calculator.CalculatePhiDirection(
                new Position(270, 90),
                new Position(270, 90)
        ));
    }

    @Test
    public void PhiIsMovedClockwiseWhenPositionsAreAlmostEqual() {
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
    public void ThetaIsMovedCounterClockwiseWhenItsValueIncreases() {
        assertEquals(Direction.CounterClockwise, calculator.CalculateThetaDirection(
                new Position(270, 90),
                new Position(300, 90)
        ));
    }

    @Test
    public void ThetaIsMovedClockwiseWhenItsValueDecreases() {
        assertEquals(Direction.Clockwise, calculator.CalculateThetaDirection(
                new Position(300, 90),
                new Position(270, 90)
        ));
    }

    @Test
    public void PhiIsMovedClockwiseWhenItsValueIncreases() {
        assertEquals(Direction.Clockwise, calculator.CalculatePhiDirection(
                new Position(270, 90),
                new Position(270, 120)
        ));
    }

    @Test
    public void PhiIsMovedCounterClockwiseWhenItsValueDecreases() {
        assertEquals(Direction.CounterClockwise, calculator.CalculatePhiDirection(
                new Position(270, 120),
                new Position(270, 90)
        ));
    }
}
