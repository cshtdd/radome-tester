package com.tddapps.rt.hardware.internal;

import com.google.inject.Inject;
import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.hardware.StepperMotor;

class StepperMotorFactorySelector implements StepperMotorFactory {
    private final ConfigurationReader configurationReader;
    final StepperMotorFactory stepperMotorFactory;
    final StepperMotorFactory stepperMotorFactorySimulator;

    @Inject
    public StepperMotorFactorySelector(
            ConfigurationReader configurationReader,
            StepperMotorFactory stepperMotorFactory,
            StepperMotorFactory stepperMotorFactorySimulator) {
        this.configurationReader = configurationReader;
        this.stepperMotorFactory = stepperMotorFactory;
        this.stepperMotorFactorySimulator = stepperMotorFactorySimulator;
    }

    @Override
    public StepperMotor createTheta() throws InvalidOperationException {
        return selectFactory().createTheta();
    }

    @Override
    public StepperMotor createPhi() throws InvalidOperationException {
        return selectFactory().createPhi();
    }

    private StepperMotorFactory selectFactory(){
        if (configurationReader.read().isSimulation()){
            return stepperMotorFactorySimulator;
        }

        return stepperMotorFactory;
    }
}
