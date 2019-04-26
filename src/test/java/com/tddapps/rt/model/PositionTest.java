package com.tddapps.rt.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PositionTest {
    @Test
    public void CanBeCloned(){
        var p1 = new Position(270, 90);
        var p1Clone = p1.toBuilder().build();
        assertEquals(p1, p1Clone);
    }

    @Test
    public void IndividualFieldsCanBeSet(){
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
    public void ValidatesThetaRanges(){
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
    public void ValidatesPhiRanges(){
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
}
