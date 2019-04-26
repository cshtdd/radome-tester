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
}
