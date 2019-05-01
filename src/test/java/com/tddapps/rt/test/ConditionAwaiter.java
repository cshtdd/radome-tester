package com.tddapps.rt.test;

import java.util.function.BooleanSupplier;

public abstract class ConditionAwaiter {
    public static boolean Await(BooleanSupplier condition){
        return Await(condition, 500);
    }

    public static boolean Await(BooleanSupplier condition, int timeoutMs){
        for (int i = 0; i < timeoutMs; i++) {
            if (condition.getAsBoolean()){
                return true;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                return false;
            }
        }

        return false;
    }
}