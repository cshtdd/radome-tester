package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.StepperMotor;
import com.tddapps.rt.hardware.StepperMotorSimulator;

class StepperMotorFactorySimulator implements StepperMotorFactory {
    @Override
    public StepperMotor CreateTheta() {
        return new StepperMotorSimulator("Theta");
    }

    @Override
    public StepperMotor CreatePhi() {
        return new StepperMotorSimulator("Phi");
    }
}
