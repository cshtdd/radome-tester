package com.tddapps.rt.utils;

import org.junit.Test;

import static com.tddapps.rt.utils.Round.AllowedError;
import static com.tddapps.rt.utils.Round.ToPrecision;

public class RoundTest {
    private static void assertEquals(double expected, double actual){
        org.junit.Assert.assertEquals(expected, actual, 0.001);
    }

    @Test
    public void CanRoundToOneDecimalPlaces(){
        assertEquals(0.0, ToPrecision(0, 1));
        assertEquals(1.0, ToPrecision(1, 1));
        assertEquals(3.4, ToPrecision(3.39999, 1));
        assertEquals(3.4, ToPrecision(3.35, 1));
        assertEquals(3.3, ToPrecision(3.349, 1));
        assertEquals(3.3, ToPrecision(3.34, 1));
    }

    @Test
    public void CanRoundToTwoDecimalPlaces(){
        assertEquals(0.0, ToPrecision(0, 2));
        assertEquals(1.0, ToPrecision(1, 2));
        assertEquals(3.40, ToPrecision(3.39999, 2));
        assertEquals(3.35, ToPrecision(3.34999, 2));
        assertEquals(3.35, ToPrecision(3.345, 2));
        assertEquals(3.34, ToPrecision(3.344, 2));
    }

    @Test
    public void CanRoundToThreeDecimalPlaces(){
        assertEquals(0.0, ToPrecision(0, 3));
        assertEquals(1.0, ToPrecision(1, 3));
        assertEquals(3.40, ToPrecision(3.39999, 3));
        assertEquals(3.35, ToPrecision(3.34999, 3));
        assertEquals(3.345, ToPrecision(3.3454, 3));
        assertEquals(3.345, ToPrecision(3.3449, 3));
        assertEquals(3.344, ToPrecision(3.34409, 3));
    }

    @Test
    public void CanCalculateThePrecision(){
        assertEquals(1, AllowedError(0));
        assertEquals(0.1, AllowedError(1));
        assertEquals(0.01, AllowedError(2));
        assertEquals(0.001, AllowedError(3));
        assertEquals(0.0001, AllowedError(4));
    }
}
