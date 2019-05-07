package com.tddapps.rt.hardware.internal;

interface StepperPrecisionRepository {
    Precision ReadTheta();
    void SaveTheta(Precision precision);

    Precision ReadPhi();
    void SavePhi(Precision precision);
}
