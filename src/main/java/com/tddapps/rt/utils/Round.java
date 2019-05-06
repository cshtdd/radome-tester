package com.tddapps.rt.utils;

public abstract class Round {
    public static double PrecisionValue(int decimalPlaces){
        return 1.0 / Math.pow(10.0, decimalPlaces);
    }

    public static double ToPrecision(double value, int decimalPlaces){
        double tenthPower = Math.pow(10.0, decimalPlaces);

        return Math.round(value * tenthPower) / tenthPower;
    }
}
