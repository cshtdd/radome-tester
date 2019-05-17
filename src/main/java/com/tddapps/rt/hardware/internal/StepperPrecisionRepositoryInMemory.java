package com.tddapps.rt.hardware.internal;

class StepperPrecisionRepositoryInMemory implements StepperPrecisionRepository {
    private final Object criticalSectionTheta = new Object();
    private Precision precisionTheta = null;

    private final Object criticalSectionPhi = new Object();
    private Precision precisionPhi = null;

    @Override
    public Precision readTheta() {
        synchronized (criticalSectionTheta){
            return precisionTheta;
        }
    }

    @Override
    public void saveTheta(Precision precision) {
        synchronized (criticalSectionTheta){
            precisionTheta = precision;
        }
    }

    @Override
    public Precision readPhi() {
        synchronized (criticalSectionPhi){
            return precisionPhi;
        }
    }

    @Override
    public void savePhi(Precision precision) {
        synchronized (criticalSectionPhi){
            precisionPhi = precision;
        }
    }
}
