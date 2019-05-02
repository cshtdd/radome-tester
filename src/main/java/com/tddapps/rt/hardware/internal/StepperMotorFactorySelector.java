package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.hardware.StepperMotor;

class StepperMotorFactorySelector implements StepperMotorFactory {
    private final ConfigurationReader configurationReader;
    private final StepperMotorFactory stepperMotorFactory;
    private final StepperMotorFactory stepperMotorFactorySimulator;

    public StepperMotorFactorySelector(
            ConfigurationReader configurationReader,
            StepperMotorFactory stepperMotorFactory,
            StepperMotorFactory stepperMotorFactorySimulator) {
        this.configurationReader = configurationReader;
        this.stepperMotorFactory = stepperMotorFactory;
        this.stepperMotorFactorySimulator = stepperMotorFactorySimulator;
    }

    @Override
    public StepperMotor CreateTheta() throws InvalidOperationException {
        return SelectFactory().CreateTheta();
    }

    @Override
    public StepperMotor CreatePhi() throws InvalidOperationException {
        return SelectFactory().CreatePhi();
    }

    private StepperMotorFactory SelectFactory(){
        if (configurationReader.Read().isSimulation()){
            return stepperMotorFactorySimulator;
        }

        return stepperMotorFactory;
    }
}
