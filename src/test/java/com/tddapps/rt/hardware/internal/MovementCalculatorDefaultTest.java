package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Direction;
import com.tddapps.rt.model.Position;
import org.junit.Test;

import static org.junit.Assert.*;

public class MovementCalculatorDefaultTest {
    private final MovementCalculator calculator = new MovementCalculatorDefault();

    @Test
    public void thetaMovesClockwiseWhenPositionsAreEqual() {
        assertEquals(Direction.Clockwise, calculator.calculateThetaDirection(
                new Position(270, 90),
                new Position(270, 90)
        ));
    }

    @Test
    public void thetaMovesClockwiseWhenPositionsAreAlmostEqual() {
        assertEquals(Direction.Clockwise, calculator.calculateThetaDirection(
                new Position(270, 90),
                new Position(270.01, 90)
        ));

        assertEquals(Direction.Clockwise, calculator.calculateThetaDirection(
                new Position(270, 90),
                new Position(269.99, 90)
        ));

        assertEquals(Direction.Clockwise, calculator.calculateThetaDirection(
                new Position(270.01, 90),
                new Position(270, 90)
        ));

        assertEquals(Direction.Clockwise, calculator.calculateThetaDirection(
                new Position(269.99, 90),
                new Position(270, 90)
        ));
    }

    @Test
    public void phiMovesClockwiseWhenPositionsAreEqual() {
        assertEquals(Direction.Clockwise, calculator.calculatePhiDirection(
                new Position(270, 90),
                new Position(270, 90)
        ));
    }

    @Test
    public void phiMovesClockwiseWhenPositionsAreAlmostEqual() {
        assertEquals(Direction.Clockwise, calculator.calculatePhiDirection(
                new Position(270, 90),
                new Position(270, 90.01)
        ));

        assertEquals(Direction.Clockwise, calculator.calculatePhiDirection(
                new Position(270, 90),
                new Position(270, 89.99)
        ));

        assertEquals(Direction.Clockwise, calculator.calculatePhiDirection(
                new Position(270, 90.01),
                new Position(270, 90)
        ));

        assertEquals(Direction.Clockwise, calculator.calculatePhiDirection(
                new Position(270, 89.99),
                new Position(270, 90)
        ));
    }

    @Test
    public void thetaMovesCounterClockwiseWhenItsValueIncreases() {
        assertEquals(Direction.CounterClockwise, calculator.calculateThetaDirection(
                new Position(270, 90),
                new Position(300, 90)
        ));
    }

    @Test
    public void thetaMovesClockwiseWhenItsValueDecreases() {
        assertEquals(Direction.Clockwise, calculator.calculateThetaDirection(
                new Position(300, 90),
                new Position(270, 90)
        ));
    }

    @Test
    public void phiMovesClockwiseWhenItsValueIncreases() {
        assertEquals(Direction.Clockwise, calculator.calculatePhiDirection(
                new Position(270, 90),
                new Position(270, 120)
        ));
    }

    @Test
    public void phiMovesCounterClockwiseWhenItsValueDecreases() {
        assertEquals(Direction.CounterClockwise, calculator.calculatePhiDirection(
                new Position(270, 120),
                new Position(270, 90)
        ));
    }

    @Test
    public void noThetaStepsAreNecessaryWhenPositionsAreAlmostEqual(){
        var precision = new Precision(10, 0.01);

        assertEquals(0, calculator.calculateThetaSteps(
                new Position(270, 90),
                new Position(270, 90),
                precision
        ));

        assertEquals(0, calculator.calculateThetaSteps(
                new Position(270, 90),
                new Position(270.01, 90),
                precision
        ));

        assertEquals(0, calculator.calculateThetaSteps(
                new Position(270.01, 90),
                new Position(270, 90),
                precision
        ));
    }

    @Test
    public void calculateThetaSteps(){
        assertEquals(1000, calculator.calculateThetaSteps(
                new Position(270, 90),
                new Position(271, 90),
                new Precision(10, 0.01)
        ));

        assertEquals(1000, calculator.calculateThetaSteps(
                new Position(271, 90),
                new Position(270, 90),
                new Precision(10, 0.01)
        ));


        assertEquals(50, calculator.calculateThetaSteps(
                new Position(270, 90),
                new Position(271, 90),
                new Precision(5, 0.1)
        ));


        assertEquals(147, calculator.calculateThetaSteps(
                new Position(270, 90),
                new Position(273.45, 90),
                new Precision(3, 0.07)
        ));
    }

    @Test
    public void calculatePhiSteps(){
        assertEquals(1000, calculator.calculatePhiSteps(
                new Position(270, 90),
                new Position(270, 91),
                new Precision(10, 0.01)
        ));

        assertEquals(1000, calculator.calculatePhiSteps(
                new Position(270, 91),
                new Position(270, 90),
                new Precision(10, 0.01)
        ));


        assertEquals(50, calculator.calculatePhiSteps(
                new Position(270, 90),
                new Position(270, 91),
                new Precision(5, 0.1)
        ));


        assertEquals(1165, calculator.calculatePhiSteps(
                new Position(270, 90),
                new Position(270, 93.33),
                new Precision(7, 0.02)
        ));
    }
}
