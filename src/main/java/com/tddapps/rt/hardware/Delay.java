package com.tddapps.rt.hardware;

public interface Delay {
    void waitMs(int ms);
    default void yield(){
        waitMs(10);
    }
}
