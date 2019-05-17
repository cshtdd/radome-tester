package com.tddapps.rt.hardware.internal;

interface StepperPrecisionRepository {
    Precision readTheta();
    void saveTheta(Precision precision);

    Precision readPhi();
    void savePhi(Precision precision);
}
