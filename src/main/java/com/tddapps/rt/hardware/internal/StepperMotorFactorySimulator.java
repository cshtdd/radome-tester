package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.StepperMotor;
import com.tddapps.rt.hardware.StepperMotorSimulator;

class StepperMotorFactorySimulator implements StepperMotorFactory {
    @Override
    public StepperMotor createTheta() {
        return new StepperMotorSimulator("Theta");
    }

    @Override
    public StepperMotor createPhi() {
        return new StepperMotorSimulator("Phi");
    }
}
