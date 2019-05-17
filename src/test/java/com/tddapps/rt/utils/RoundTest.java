package com.tddapps.rt.utils;

import org.junit.Test;

import static com.tddapps.rt.utils.Round.allowedError;
import static com.tddapps.rt.utils.Round.toPrecision;

public class RoundTest {
    private static void assertEquals(double expected, double actual){
        org.junit.Assert.assertEquals(expected, actual, 0.001);
    }

    @Test
    public void canRoundToOneDecimalPlaces(){
        assertEquals(0.0, toPrecision(0, 1));
        assertEquals(1.0, toPrecision(1, 1));
        assertEquals(3.4, toPrecision(3.39999, 1));
        assertEquals(3.4, toPrecision(3.35, 1));
        assertEquals(3.3, toPrecision(3.349, 1));
        assertEquals(3.3, toPrecision(3.34, 1));
    }

    @Test
    public void canRoundToTwoDecimalPlaces(){
        assertEquals(0.0, toPrecision(0, 2));
        assertEquals(1.0, toPrecision(1, 2));
        assertEquals(3.40, toPrecision(3.39999, 2));
        assertEquals(3.35, toPrecision(3.34999, 2));
        assertEquals(3.35, toPrecision(3.345, 2));
        assertEquals(3.34, toPrecision(3.344, 2));
    }

    @Test
    public void canRoundToThreeDecimalPlaces(){
        assertEquals(0.0, toPrecision(0, 3));
        assertEquals(1.0, toPrecision(1, 3));
        assertEquals(3.40, toPrecision(3.39999, 3));
        assertEquals(3.35, toPrecision(3.34999, 3));
        assertEquals(3.345, toPrecision(3.3454, 3));
        assertEquals(3.345, toPrecision(3.3449, 3));
        assertEquals(3.344, toPrecision(3.34409, 3));
    }

    @Test
    public void canCalculateThePrecision(){
        assertEquals(1, allowedError(0));
        assertEquals(0.1, allowedError(1));
        assertEquals(0.01, allowedError(2));
        assertEquals(0.001, allowedError(3));
        assertEquals(0.0001, allowedError(4));
    }
}
