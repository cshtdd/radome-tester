package com.tddapps.rt.hardware.internal;

class StepperPrecisionRepositoryInMemory implements StepperPrecisionRepository {
    private final Object criticalSectionTheta = new Object();
    private Precision precisionTheta = null;

    private final Object criticalSectionPhi = new Object();
    private Precision precisionPhi = null;

    @Override
    public Precision ReadTheta() {
        synchronized (criticalSectionTheta){
            return precisionTheta;
        }
    }

    @Override
    public void SaveTheta(Precision precision) {
        synchronized (criticalSectionTheta){
            precisionTheta = precision;
        }
    }

    @Override
    public Precision ReadPhi() {
        synchronized (criticalSectionPhi){
            return precisionPhi;
        }
    }

    @Override
    public void SavePhi(Precision precision) {
        synchronized (criticalSectionPhi){
            precisionPhi = precision;
        }
    }
}
