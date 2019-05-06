package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Direction;
import com.tddapps.rt.hardware.MovementDirectionCalculator;
import com.tddapps.rt.model.Position;
import org.junit.Test;
import static org.junit.Assert.*;

public class MovementDirectionCalculatorDefaultTest {
    private final MovementDirectionCalculator calculator = new MovementDirectionCalculatorDefault();

    @Test
    public void ThetaIsMovedClockwiseWhenPositionsAreEqual(){
        assertEquals(Direction.Clockwise, calculator.CalculateThetaDirection(
                new Position(270, 90),
                new Position(270, 90)
        ));
    }

    @Test
    public void ThetaIsMovedClockwiseWhenPositionsAreAlmostEqual(){
        assertEquals(Direction.Clockwise, calculator.CalculateThetaDirection(
                new Position(270, 90),
                new Position(270.01, 90)
        ));

        assertEquals(Direction.Clockwise, calculator.CalculateThetaDirection(
                new Position(270, 90),
                new Position(269.99, 90)
        ));
    }
}
