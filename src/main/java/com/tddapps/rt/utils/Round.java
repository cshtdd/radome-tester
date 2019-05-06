package com.tddapps.rt.utils;

public abstract class Round {
    public static double PrecisionValue(int decimalPlaces){
        return 1.0 / TenthPower(decimalPlaces);
    }

    public static double ToPrecision(double value, int decimalPlaces){
        double tenthPower = TenthPower(decimalPlaces);

        return Math.round(value * tenthPower) / tenthPower;
    }

    private static double TenthPower(int decimalPlaces) {
        return Math.pow(10.0, decimalPlaces);
    }
}
