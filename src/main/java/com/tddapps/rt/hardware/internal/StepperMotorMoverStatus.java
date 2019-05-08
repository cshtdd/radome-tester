package com.tddapps.rt.hardware.internal;

import com.google.inject.Inject;
import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.Direction;
import com.tddapps.rt.hardware.StepperMotor;
import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class StepperMotorMoverStatus implements StepperMotorMover {
    private final static String THETA = "Theta";
    private final static String PHI = "Phi";

    private final StatusRepository statusRepository;
    private final MovementCalculator movementCalculator;
    private final StepperPrecisionRepository stepperPrecisionRepository;

    @Inject
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
        if (!ShouldMove(THETA, currentStatus)) {
            return;
        }

        var src = currentStatus.getCurrentPosition();
        var dest = currentStatus.getCommandedPosition();
        var precision = stepperPrecisionRepository.ReadTheta();
        var direction = movementCalculator.CalculateThetaDirection(src, dest);
        var steps = movementCalculator.CalculateThetaSteps(src, dest, precision);

        LogMotorMovement(THETA, direction, steps);

        for (int i = 0; i < steps; i++) {
            Move(motor, direction);
        }

        var updatedPosition = src.toBuilder()
                .thetaDegrees(dest.getThetaDegrees())
                .build();
        CompleteMovement(THETA, updatedPosition, dest);
    }

    @Override
    public void MovePhi(StepperMotor motor) throws InvalidOperationException {
        var currentStatus = statusRepository.CurrentStatus();
        if (!ShouldMove(PHI, currentStatus)) {
            return;
        }

        var src = currentStatus.getCurrentPosition();
        var dest = currentStatus.getCommandedPosition();
        var precision = stepperPrecisionRepository.ReadPhi();
        var direction = movementCalculator.CalculatePhiDirection(src, dest);
        var steps = movementCalculator.CalculatePhiSteps(src, dest, precision);

        LogMotorMovement(PHI, direction, steps);

        for (int i = 0; i < steps; i++) {
            Move(motor, direction);
        }

        var updatedPosition = src.toBuilder()
                .phiDegrees(dest.getPhiDegrees())
                .build();
        CompleteMovement(PHI, updatedPosition, dest);
    }

    private boolean ShouldMove(String axis, Status currentStatus) {
        if (!currentStatus.isMoving()){
            return false;
        }

        log.info(String.format("Movement Started; axis=%s;", axis));

        if (!currentStatus.getCurrentPosition().IsValid()){
            HaltMovement("Invalid Current Position");
            return false;
        }

        if (!currentStatus.getCommandedPosition().IsValid()){
            HaltMovement("Invalid Commanded Position");
            return false;
        }

        return true;
    }

    private void Move(StepperMotor motor, Direction direction) throws InvalidOperationException {
        try {
            if (direction.equals(Direction.Clockwise)) {
                motor.MoveCW();
            } else {
                motor.MoveCCW();
            }
        } catch (InvalidOperationException e){
            HaltMovement("Movement Error");
            throw e;
        }
    }

    private void HaltMovement(String reason) {
        log.info(String.format("Stop Movement; reason=%s", reason));
        var updatedStatus = statusRepository.CurrentStatus()
                .toBuilder()
                .isMoving(false)
                .build();
        statusRepository.Save(updatedStatus);
    }

    private void LogMotorMovement(String axis, Direction direction, int steps) {
        log.info(String.format("Moving motor; axis=%s; direction=%s; steps=%d;", axis, direction, steps));
    }

    private void CompleteMovement(String axis, Position updatedPosition, Position commandedPosition) {
        var isStillMoving = !updatedPosition.AlmostEquals(commandedPosition);

        log.info(String.format("Movement Completed; axis=%s; isMoving=%s", axis, isStillMoving));

        var updatedStatus = statusRepository.CurrentStatus()
                .toBuilder()
                .currentPosition(updatedPosition)
                .isMoving(isStillMoving)
                .build();
        statusRepository.Save(updatedStatus);
    }
}
