package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.Direction;
import com.tddapps.rt.hardware.StepperMotor;
import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.test.StatusRepositoryStub;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class StepperMotorMoverStatusTest {
    private final StatusRepositoryStub statusRepositoryStub = new StatusRepositoryStub();
    private final MovementCalculator movementCalculatorMock = mock(MovementCalculator.class);
    private final StepperPrecisionRepository stepperPrecisionRepositoryMock = mock(StepperPrecisionRepository.class);
    private final StepperMotorMover motorMover = new StepperMotorMoverStatus(
            statusRepositoryStub,
            movementCalculatorMock,
            stepperPrecisionRepositoryMock
    );
    private final StepperMotor stepperMotorThetaMock = mock(StepperMotor.class);
    private final StepperMotor stepperMotorPhiMock = mock(StepperMotor.class);

    @Before
    public void Setup() {
        statusRepositoryStub.Save(Status.builder().build());
    }

    @Test
    public void MovesTheta() throws InvalidOperationException {
        var src = new Position(200, 90);
        var dest = new Position(270, 90);
        var precision = new Precision(25, 0.1);

        when(stepperPrecisionRepositoryMock.ReadTheta()).thenReturn(precision);
        statusRepositoryStub.CurrentStatus().setCurrentPosition(src);
        statusRepositoryStub.CurrentStatus().setCommandedPosition(dest);
        when(movementCalculatorMock.CalculateThetaDirection(src, dest)).thenReturn(Direction.CounterClockwise);
        when(movementCalculatorMock.CalculateThetaSteps(src, dest, precision)).thenReturn(65);


        motorMover.MoveTheta(stepperMotorThetaMock);


        verify(stepperMotorThetaMock, times(65)).MoveCCW();
    }
}
