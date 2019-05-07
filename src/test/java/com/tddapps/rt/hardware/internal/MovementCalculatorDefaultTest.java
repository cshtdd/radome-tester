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

    @Test
    public void NoThetaStepsAreNecessaryWhenPositionsAreAlmostEqual(){
        var precision = new Precision(10, 0.01);

        assertEquals(0, calculator.CalculateThetaSteps(
                new Position(270, 90),
                new Position(270, 90),
                precision
        ));

        assertEquals(0, calculator.CalculateThetaSteps(
                new Position(270, 90),
                new Position(270.01, 90),
                precision
        ));

        assertEquals(0, calculator.CalculateThetaSteps(
                new Position(270.01, 90),
                new Position(270, 90),
                precision
        ));
    }

    @Test
    public void CalculateThetaSteps(){
        assertEquals(1000, calculator.CalculateThetaSteps(
                new Position(270, 90),
                new Position(271, 90),
                new Precision(10, 0.01)
        ));

        assertEquals(1000, calculator.CalculateThetaSteps(
                new Position(271, 90),
                new Position(270, 90),
                new Precision(10, 0.01)
        ));


        assertEquals(50, calculator.CalculateThetaSteps(
                new Position(270, 90),
                new Position(271, 90),
                new Precision(5, 0.1)
        ));


        assertEquals(147, calculator.CalculateThetaSteps(
                new Position(270, 90),
                new Position(273.45, 90),
                new Precision(3, 0.07)
        ));
    }

    @Test
    public void CalculatePhiSteps(){
        assertEquals(1000, calculator.CalculatePhiSteps(
                new Position(270, 90),
                new Position(270, 91),
                new Precision(10, 0.01)
        ));

        assertEquals(1000, calculator.CalculatePhiSteps(
                new Position(270, 91),
                new Position(270, 90),
                new Precision(10, 0.01)
        ));


        assertEquals(50, calculator.CalculatePhiSteps(
                new Position(270, 90),
                new Position(270, 91),
                new Precision(5, 0.1)
        ));


        assertEquals(1165, calculator.CalculatePhiSteps(
                new Position(270, 90),
                new Position(270, 93.33),
                new Precision(7, 0.02)
        ));
    }
}
