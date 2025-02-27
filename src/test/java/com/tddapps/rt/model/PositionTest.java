package com.tddapps.rt.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PositionTest {
    @Test
    public void RoundsDegreeValuesToTwoDecimalPlaces(){
        var expected = new Position(193.45, 45.33);

        assertEquals(expected, new Position(193.446, 45.33333));
    }

    @Test
    public void canBeCloned(){
        var p1 = new Position(270, 90);
        var p1Clone = p1.toBuilder().build();
        assertEquals(p1, p1Clone);
    }

    @Test
    public void individualFieldsCanBeSet(){
        var top = new Position(270, 0);
        var right = new Position(180, 90);
        var center = new Position(270, 90);

        var actualTop = center.toBuilder()
                .phiDegrees(0)
                .build();
        assertEquals(top, actualTop);

        var actualRight = center.toBuilder()
                .thetaDegrees(180)
                .build();
        assertEquals(right, actualRight);
    }

    @Test
    public void validatesThetaRanges(){
        for (double theta = -150; theta <= 179.1; theta += 0.1) {
            assertFalse(new Position(theta, 90).isValid());
        }

        for (double theta = 180; theta <= 360; theta += 0.1) {
            assertTrue(new Position(theta, 90).isValid());
        }

        for (double theta = 360.1; theta <= 450; theta += 0.1) {
            assertFalse(new Position(theta, 90).isValid());
        }
    }

    @Test
    public void validatesPhiRanges(){
        for (double phi = -45; phi <= -0.1; phi += 0.1) {
            assertFalse(new Position(270, phi).isValid());
        }

        for (double phi = 0; phi <= 180; phi += 0.1) {
            assertTrue(new Position(270, phi).isValid());
        }

        for (double phi = 180.1; phi <= 450; phi += 0.1) {
            assertFalse(new Position(270, phi).isValid());
        }
    }

    @Test
    public void areAlmostEquals(){
        assertFalse(new Position(180.00, 90.00).almostEquals(
                new Position(179.98, 90.00)
        ));

        assertTrue(new Position(180.00, 90.00).almostEquals(
                new Position(179.99, 90.00)
        ));

        assertTrue(new Position(180.00, 90.00).almostEquals(
                new Position(180.00, 90.00)
        ));

        assertTrue(new Position(180.00, 90.00).almostEquals(
                new Position(180.01, 90.00)
        ));

        assertFalse(new Position(180.00, 90.00).almostEquals(
                new Position(180.02, 90.00)
        ));


        assertFalse(new Position(180.00, 90.00).almostEquals(
                new Position(180.00, 89.98)
        ));

        assertTrue(new Position(180.00, 90.00).almostEquals(
                new Position(180.00, 89.99)
        ));

        assertTrue(new Position(180.00, 90.00).almostEquals(
                new Position(180.00, 90.00)
        ));

        assertTrue(new Position(180.00, 90.00).almostEquals(
                new Position(180.00, 90.01)
        ));

        assertFalse(new Position(180.00, 90.00).almostEquals(
                new Position(180.00, 90.02)
        ));
    }
}
