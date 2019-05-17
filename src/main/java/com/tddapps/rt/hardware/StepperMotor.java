package com.tddapps.rt.hardware;

import com.tddapps.rt.InvalidOperationException;

public interface StepperMotor {
    void init();
    void destroy();

    boolean moveCW() throws InvalidOperationException;
    boolean moveCCW() throws InvalidOperationException;
}
