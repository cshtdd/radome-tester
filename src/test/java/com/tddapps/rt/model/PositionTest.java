package com.tddapps.rt.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PositionTest {
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
