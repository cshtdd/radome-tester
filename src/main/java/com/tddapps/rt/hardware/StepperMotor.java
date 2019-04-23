package com.tddapps.rt.hardware;

public interface StepperMotor {
    void Init();
    void Destroy();

    boolean MoveCW() throws InvalidOperationException;
    boolean MoveCCW() throws InvalidOperationException;
}
