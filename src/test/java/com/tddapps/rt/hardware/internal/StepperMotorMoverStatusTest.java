package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.StepperMotor;
import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.test.StatusRepositoryStub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class StepperMotorMoverStatusTest {
    private final Precision seededPrecision = new Precision(1, 1);

    private final StatusRepositoryStub statusRepositoryStub = new StatusRepositoryStub();
    private final MovementCalculator movementCalculator = new MovementCalculatorDefault();
    private final StepperPrecisionRepository stepperPrecisionRepositoryMock = mock(StepperPrecisionRepository.class);
    private final StepperMotorMover motorMover = new StepperMotorMoverStatus(
            statusRepositoryStub,
            movementCalculator,
            stepperPrecisionRepositoryMock
    );
    private final StepperMotor stepperMotorThetaMock = mock(StepperMotor.class);
    private final StepperMotor stepperMotorPhiMock = mock(StepperMotor.class);

    private Status status() {
        return statusRepositoryStub.CurrentStatus();
    }

    @Before
    public void Setup() {
        statusRepositoryStub.Save(Status.builder().build());
        when(stepperPrecisionRepositoryMock.ReadTheta()).thenReturn(seededPrecision);
    }

    @Test
    public void MovesThetaCounterClockwise() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(200, 90));
        status().setCommandedPosition(new Position(270, 90));

        motorMover.MoveTheta(stepperMotorThetaMock);

        verify(stepperMotorThetaMock, times(70)).MoveCCW();
    }

    @Test
    public void MovesThetaClockwise() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(220, 90));
        status().setCommandedPosition(new Position(200, 90));

        motorMover.MoveTheta(stepperMotorThetaMock);

        verify(stepperMotorThetaMock, times(20)).MoveCW();
    }

    @Test
    public void UpdatesStatusCurrentPosition() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(200, 50));
        status().setCommandedPosition(new Position(270, 90));

        motorMover.MoveTheta(stepperMotorThetaMock);

        assertEquals(new Position(270, 50), status().getCurrentPosition());
    }

    @Test
    public void DoesNotMoveWhenStatusIsNotMoving() throws InvalidOperationException {
        status().setCurrentPosition(new Position(200, 50));
        status().setCommandedPosition(new Position(270, 90));

        motorMover.MoveTheta(stepperMotorThetaMock);

        verify(stepperMotorThetaMock, times(0)).MoveCW();
        verify(stepperMotorThetaMock, times(0)).MoveCCW();
    }
}
