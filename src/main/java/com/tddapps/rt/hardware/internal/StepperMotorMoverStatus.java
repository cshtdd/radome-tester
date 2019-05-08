package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.StepperMotor;
import com.tddapps.rt.model.StatusRepository;

public class StepperMotorMoverStatus implements StepperMotorMover {
    private final StatusRepository statusRepository;
    private final MovementCalculator movementCalculator;
    private final StepperPrecisionRepository stepperPrecisionRepository;

    public StepperMotorMoverStatus(
            StatusRepository statusRepository,
            MovementCalculator movementCalculator,
            StepperPrecisionRepository stepperPrecisionRepository) {

        this.statusRepository = statusRepository;
        this.movementCalculator = movementCalculator;
        this.stepperPrecisionRepository = stepperPrecisionRepository;
    }

    @Override
    public void MoveTheta(StepperMotor motor) throws InvalidOperationException {
        for (int i = 0; i < 65; i++) {
            motor.MoveCCW();
        }
    }

    @Override
    public void MovePhi(StepperMotor motor) throws InvalidOperationException {

    }
}
