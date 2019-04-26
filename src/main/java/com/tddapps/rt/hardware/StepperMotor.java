package com.tddapps.rt.hardware;

import com.tddapps.rt.InvalidOperationException;

public interface StepperMotor {
    void Init();
    void Destroy();

    boolean MoveCW() throws InvalidOperationException;
    boolean MoveCCW() throws InvalidOperationException;
}
