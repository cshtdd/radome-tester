package com.tddapps.rt.utils;

public abstract class Round {
    public static double allowedError(int decimalPlaces){
        return 1.0 / tenthPower(decimalPlaces);
    }

    public static double toPrecision(double value, int decimalPlaces){
        double tenthPower = tenthPower(decimalPlaces);

        return Math.round(value * tenthPower) / tenthPower;
    }

    private static double tenthPower(int decimalPlaces) {
        return Math.pow(10.0, decimalPlaces);
    }
}
