package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.Direction;
import com.tddapps.rt.hardware.StepperMotor;
import com.tddapps.rt.model.Position;
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
        var currentStatus = statusRepository.CurrentStatus();

        if (!currentStatus.isMoving()){
            // TODO, maybe log something
            return;
        }

        var src = currentStatus.getCurrentPosition();
        var dest = currentStatus.getCommandedPosition();

        if (!src.IsValid()){
            // TODO, log something
            var updatedStatus = statusRepository.CurrentStatus()
                    .toBuilder()
                    .isMoving(false)
                    .build();
            statusRepository.Save(updatedStatus);
            return;
        }

        if (!dest.IsValid()){
            // TODO, log something
            var updatedStatus = statusRepository.CurrentStatus()
                    .toBuilder()
                    .isMoving(false)
                    .build();
            statusRepository.Save(updatedStatus);
            return;
        }

        var precision = stepperPrecisionRepository.ReadTheta();
        var direction = movementCalculator.CalculateThetaDirection(src, dest);
        var steps = movementCalculator.CalculateThetaSteps(src, dest, precision);

        for (int i = 0; i < steps; i++) {
            if (direction.equals(Direction.Clockwise)){
                motor.MoveCW();
            } else {
                motor.MoveCCW();
            }
        }

        var updatedPosition = src.toBuilder()
                .thetaDegrees(dest.getThetaDegrees())
                .build();
        var isMoving = !updatedPosition.AlmostEquals(dest);
        var updatedStatus = statusRepository.CurrentStatus()
                .toBuilder()
                .currentPosition(updatedPosition)
                .isMoving(isMoving)
                .build();
        statusRepository.Save(updatedStatus);
    }

    @Override
    public void MovePhi(StepperMotor motor) throws InvalidOperationException {

    }
}
