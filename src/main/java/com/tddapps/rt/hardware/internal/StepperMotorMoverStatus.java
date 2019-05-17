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
    public void moveTheta(StepperMotor motor) throws InvalidOperationException {
        var currentStatus = statusRepository.read();
        if (!shouldMove(THETA, currentStatus)) {
            return;
        }

        var src = currentStatus.getCurrentPosition();
        var dest = currentStatus.getCommandedPosition();
        var precision = stepperPrecisionRepository.readTheta();
        var direction = movementCalculator.calculateThetaDirection(src, dest);
        var steps = movementCalculator.calculateThetaSteps(src, dest, precision);

        logMotorMovement(THETA, direction, steps);

        for (int i = 0; i < steps; i++) {
            move(motor, direction);
        }

        var updatedPosition = src.toBuilder()
                .thetaDegrees(dest.getThetaDegrees())
                .build();
        completeMovement(THETA, updatedPosition, dest);
    }

    @Override
    public void movePhi(StepperMotor motor) throws InvalidOperationException {
        var currentStatus = statusRepository.read();
        if (!shouldMove(PHI, currentStatus)) {
            return;
        }

        var src = currentStatus.getCurrentPosition();
        var dest = currentStatus.getCommandedPosition();
        var precision = stepperPrecisionRepository.readPhi();
        var direction = movementCalculator.calculatePhiDirection(src, dest);
        var steps = movementCalculator.calculatePhiSteps(src, dest, precision);

        logMotorMovement(PHI, direction, steps);

        for (int i = 0; i < steps; i++) {
            move(motor, direction);
        }

        var updatedPosition = src.toBuilder()
                .phiDegrees(dest.getPhiDegrees())
                .build();
        completeMovement(PHI, updatedPosition, dest);
    }

    private boolean shouldMove(String axis, Status currentStatus) {
        if (!currentStatus.isMoving()){
            return false;
        }

        log.debug(String.format("Movement Started; axis=%s;", axis));

        if (!currentStatus.getCurrentPosition().isValid()){
            haltMovement("Invalid Current Position");
            return false;
        }

        if (!currentStatus.getCommandedPosition().isValid()){
            haltMovement("Invalid Commanded Position");
            return false;
        }

        return true;
    }

    private void move(StepperMotor motor, Direction direction) throws InvalidOperationException {
        try {
            if (direction.equals(Direction.Clockwise)) {
                motor.moveCW();
            } else {
                motor.moveCCW();
            }
        } catch (InvalidOperationException e){
            haltMovement("Movement Error");
            throw e;
        }
    }

    private void haltMovement(String reason) {
        log.info(String.format("Stop Movement; reason=%s", reason));
        statusRepository.update(status -> status
                .toBuilder()
                .isMoving(false)
                .build());
    }

    private void logMotorMovement(String axis, Direction direction, int steps) {
        log.debug(String.format("Moving motor; axis=%s; direction=%s; steps=%d;", axis, direction, steps));
    }

    private void completeMovement(String axis, Position updatedPosition, Position commandedPosition) {
        var isStillMoving = !updatedPosition.almostEquals(commandedPosition);

        log.debug(String.format("Movement Completed; axis=%s; isMoving=%s", axis, isStillMoving));

        statusRepository.update(status -> status
                .toBuilder()
                .currentPosition(updatedPosition)
                .isMoving(isStillMoving)
                .build());
    }
}
