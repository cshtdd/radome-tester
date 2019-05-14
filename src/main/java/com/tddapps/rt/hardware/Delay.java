package com.tddapps.rt.hardware;

public interface Delay {
    void Wait(int ms);
    default void Yield(){
        Wait(10);
    }
}
