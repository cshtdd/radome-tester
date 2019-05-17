package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.StepperMotor;
import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.test.StatusRepositoryStub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
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
        return statusRepositoryStub.read();
    }

    private void assertNoMotorMoved() throws InvalidOperationException {
        verify(stepperMotorThetaMock, times(0)).moveCW();
        verify(stepperMotorThetaMock, times(0)).moveCCW();
        verify(stepperMotorPhiMock, times(0)).moveCW();
        verify(stepperMotorPhiMock, times(0)).moveCCW();
    }

    @Before
    public void setup() {
        statusRepositoryStub.save(Status.builder().build());
        when(stepperPrecisionRepositoryMock.readTheta()).thenReturn(seededPrecision);
        when(stepperPrecisionRepositoryMock.readPhi()).thenReturn(seededPrecision);
    }

    @Test
    public void movesThetaCounterClockwise() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(200, 90));
        status().setCommandedPosition(new Position(270, 90));

        motorMover.moveTheta(stepperMotorThetaMock);

        verify(stepperMotorThetaMock, times(70)).moveCCW();
    }

    @Test
    public void movesThetaClockwise() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(220, 90));
        status().setCommandedPosition(new Position(200, 90));

        motorMover.moveTheta(stepperMotorThetaMock);

        verify(stepperMotorThetaMock, times(20)).moveCW();
    }

    @Test
    public void moveThetaUpdatesCurrentPosition() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(200, 50));
        status().setCommandedPosition(new Position(270, 90));

        motorMover.moveTheta(stepperMotorThetaMock);

        assertEquals(new Position(270, 50), status().getCurrentPosition());
    }

    @Test
    public void moveThetaSetsIsMovingToFalseWhenPositionsMatchAfterMovement() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(200, 90));
        status().setCommandedPosition(new Position(270, 90));

        motorMover.moveTheta(stepperMotorThetaMock);

        assertFalse(status().isMoving());
    }

    @Test
    public void moveThetaKeepsIsMovingTrueWhenPositionsDoNotMatchAfterMovement() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(200, 50));
        status().setCommandedPosition(new Position(270, 90));

        motorMover.moveTheta(stepperMotorThetaMock);

        assertTrue(status().isMoving());
    }

    @Test
    public void moveThetaBubblesUpMovementExceptions() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(270, 50));
        status().setCommandedPosition(new Position(260, 90));
        doThrow(new InvalidOperationException()).when(stepperMotorThetaMock).moveCW();

        try {
            motorMover.moveTheta(stepperMotorThetaMock);
            fail("Should have thrown");
        }catch (InvalidOperationException e){
            assertNotNull(e);
        }

        assertFalse(status().isMoving());
    }


    @Test
    public void movesPhiCounterClockwise() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(250, 90));
        status().setCommandedPosition(new Position(250, 20));

        motorMover.movePhi(stepperMotorPhiMock);

        verify(stepperMotorPhiMock, times(70)).moveCCW();
    }

    @Test
    public void movesPhiClockwise() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(200, 90));
        status().setCommandedPosition(new Position(200, 110));

        motorMover.movePhi(stepperMotorPhiMock);

        verify(stepperMotorPhiMock, times(20)).moveCW();
    }

    @Test
    public void movePhiUpdatesCurrentPosition() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(200, 50));
        status().setCommandedPosition(new Position(250, 90));

        motorMover.movePhi(stepperMotorPhiMock);

        assertEquals(new Position(200, 90), status().getCurrentPosition());
    }

    @Test
    public void movePhiSetsIsMovingToFalseWhenPositionsMatchAfterMovement() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(200, 50));
        status().setCommandedPosition(new Position(200, 90));

        motorMover.movePhi(stepperMotorPhiMock);

        assertFalse(status().isMoving());
    }

    @Test
    public void movePhiKeepsIsMovingTrueWhenPositionsDoNotMatchAfterMovement() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(200, 50));
        status().setCommandedPosition(new Position(270, 90));

        motorMover.movePhi(stepperMotorPhiMock);

        assertTrue(status().isMoving());
    }

    @Test
    public void movePhiBubblesUpMovementExceptions() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(270, 50));
        status().setCommandedPosition(new Position(260, 90));
        doThrow(new InvalidOperationException()).when(stepperMotorPhiMock).moveCW();

        try {
            motorMover.movePhi(stepperMotorPhiMock);
            fail("Should have thrown");
        }catch (InvalidOperationException e){
            assertNotNull(e);
        }

        assertFalse(status().isMoving());
    }


    @Test
    public void doesNotMoveWhenStatusIsNotMoving() throws InvalidOperationException {
        status().setCurrentPosition(new Position(200, 50));
        status().setCommandedPosition(new Position(270, 90));

        motorMover.moveTheta(stepperMotorThetaMock);
        motorMover.movePhi(stepperMotorPhiMock);

        assertNoMotorMoved();
        assertFalse(status().isMoving());
    }

    @Test
    public void doesNotMoveWhenCurrentPositionIsInvalid() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(720, 50));
        status().setCommandedPosition(new Position(270, 90));

        motorMover.moveTheta(stepperMotorThetaMock);
        motorMover.movePhi(stepperMotorPhiMock);

        assertNoMotorMoved();
        assertFalse(status().isMoving());
    }

    @Test
    public void doesNotMoveWhenCommandedPositionIsInvalid() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(270, 50));
        status().setCommandedPosition(new Position(-4, 90));

        motorMover.moveTheta(stepperMotorThetaMock);
        motorMover.movePhi(stepperMotorPhiMock);

        assertNoMotorMoved();
        assertFalse(status().isMoving());
    }

    @Test
    public void doesNotMoveWhenPositionsAreAlmostEqual() throws InvalidOperationException {
        status().setMoving(true);
        status().setCurrentPosition(new Position(270, 50));
        status().setCommandedPosition(new Position(270.01, 50.01));

        motorMover.moveTheta(stepperMotorThetaMock);
        motorMover.movePhi(stepperMotorPhiMock);

        assertNoMotorMoved();
        assertFalse(status().isMoving());
    }
}
