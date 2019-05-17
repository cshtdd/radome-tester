package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.DelaySimulator;
import com.tddapps.rt.hardware.StepperMotor;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;
import com.tddapps.rt.test.StatusRepositoryStub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MovementDaemonEventLoopTest {
    private final StatusRepository statusRepository = new StatusRepositoryStub();
    private final StepperMotorFactory stepperMotorFactoryMock = mock(StepperMotorFactory.class);
    private final CalibrationService calibrationServiceMock = mock(CalibrationService.class);
    private final StepperMotorMover stepperMotorMoverMock = mock(StepperMotorMover.class);

    private final StepperMotor thetaStepper = mock(StepperMotor.class);
    private final StepperMotor phiStepper = mock(StepperMotor.class);

    private final MovementDaemonEventLoopTestable daemon = new MovementDaemonEventLoopTestable(
            statusRepository,
            new DelaySimulator(),
            stepperMotorFactoryMock,
            calibrationServiceMock,
            stepperMotorMoverMock
    );

    private Status status() {
        return statusRepository.read();
    }

    @Before
    public void setup() throws InvalidOperationException {
        statusRepository.save(Status.builder().build());

        when(stepperMotorFactoryMock.createTheta()).thenReturn(thetaStepper);
        when(stepperMotorFactoryMock.createPhi()).thenReturn(phiStepper);
    }

    private static class MovementDaemonEventLoopTestable extends MovementDaemonEventLoop {
        public int maxIterations = 1;
        public int currentIteration = 0;
        public RuntimeException seededException;

        public MovementDaemonEventLoopTestable(
                StatusRepository statusRepository,
                Delay delay,
                StepperMotorFactory stepperMotorFactory,
                CalibrationService calibrationServiceMock,
                StepperMotorMover stepperMotorMoverMock) {
            super(statusRepository, delay, stepperMotorFactory, calibrationServiceMock, stepperMotorMoverMock);
        }

        @Override
        protected boolean runCondition() {
            if (seededException!=null){
                throw seededException;
            }

            return currentIteration++ < maxIterations;
        }
    }

    @Test
    public void runFinishesBecauseItEvaluatesCondition() {
        daemon.maxIterations = 10;

        daemon.run();

        assertEquals(11, daemon.currentIteration);
    }

    @Test
    public void runWillNotDoAnythingIfHardwareHasAlreadyBeenInitialized() {
        status().setHardwareInitialized(true);

        daemon.run();

        assertEquals(0, daemon.currentIteration);
    }

    @Test
    public void createsThetaMotor() throws InvalidOperationException {
        daemon.run();

        verify(stepperMotorFactoryMock).createTheta();
    }

    @Test
    public void initializesThetaMotor() throws InvalidOperationException {
        daemon.run();

        verify(thetaStepper).init();
    }

    @Test
    public void thetaMotorCreationFailureChangesStatus() throws InvalidOperationException {
        when(stepperMotorFactoryMock.createTheta()).thenThrow(new InvalidOperationException());

        daemon.run();

        assertEquals(0, daemon.currentIteration);
        assertFalse(status().isHardwareInitialized());
        assertTrue(status().isHardwareCrash());
    }

    @Test
    public void createsPhiMotor() throws InvalidOperationException {
        daemon.run();

        verify(stepperMotorFactoryMock).createPhi();
    }

    @Test
    public void initializesPhiMotor() throws InvalidOperationException {
        daemon.run();

        verify(phiStepper).init();
    }

    @Test
    public void phiMotorCreationFailureChangesStatus() throws InvalidOperationException {
        when(stepperMotorFactoryMock.createPhi()).thenThrow(new InvalidOperationException());

        daemon.run();

        assertEquals(0, daemon.currentIteration);
        assertFalse(status().isHardwareInitialized());
        assertTrue(status().isHardwareCrash());
    }

    @Test
    public void setsHardwareInitializedStatus(){
        daemon.run();

        assertTrue(daemon.currentIteration > 0);
        assertTrue(status().isHardwareInitialized());
        assertFalse(status().isHardwareCrash());
    }

    @Test
    public void setsHardwareCrashWhenUnexpectedErrorOccurs(){
        daemon.seededException = new RuntimeException();

        daemon.run();

        assertTrue(status().isHardwareInitialized());
        assertTrue(status().isHardwareCrash());
        verify(thetaStepper).destroy();
        verify(phiStepper).destroy();
    }

    @Test
    public void calibratesTheta() throws InvalidOperationException {
        daemon.run();

        verify(calibrationServiceMock).calibrateThetaStepper(thetaStepper);
        assertFalse(status().isCalibrating());
        assertTrue(status().isCalibrated());
    }

    @Test
    public void setsHardwareCrashWhenThetaCalibrationFails() throws InvalidOperationException {
        doThrow(new InvalidOperationException())
                .when(calibrationServiceMock)
                .calibrateThetaStepper(any());

        daemon.run();

        assertTrue(status().isCalibrating());
        assertFalse(status().isHardwareInitialized());
        assertTrue(status().isHardwareCrash());
        verify(thetaStepper).destroy();
        verify(phiStepper).destroy();
    }

    @Test
    public void calibratesPhi() throws InvalidOperationException {
        daemon.run();

        verify(calibrationServiceMock).calibratePhiStepper(phiStepper);
        assertFalse(status().isCalibrating());
        assertTrue(status().isCalibrated());
    }

    @Test
    public void setsHardwareCrashWhenPhiCalibrationFails() throws InvalidOperationException {
        doThrow(new InvalidOperationException())
                .when(calibrationServiceMock)
                .calibratePhiStepper(any());

        daemon.run();

        assertTrue(status().isCalibrating());
        assertFalse(status().isHardwareInitialized());
        assertTrue(status().isHardwareCrash());
        verify(thetaStepper).destroy();
        verify(phiStepper).destroy();
    }

    @Test
    public void movesThetaOnTheLoop() throws InvalidOperationException {
        daemon.maxIterations = 10;

        daemon.run();

        verify(stepperMotorMoverMock, times(10)).moveTheta(thetaStepper);
    }

    @Test
    public void setsHardwareCrashWhenThetaMovementFails() throws InvalidOperationException {
        daemon.maxIterations = 10;

        doThrow(new InvalidOperationException())
                .when(stepperMotorMoverMock)
                .moveTheta(any());

        daemon.run();

        assertTrue(status().isHardwareCrash());
        assertEquals(1, daemon.currentIteration);
        verify(thetaStepper).destroy();
        verify(phiStepper).destroy();
    }

    @Test
    public void movesPhiOnTheLoop() throws InvalidOperationException {
        daemon.maxIterations = 10;

        daemon.run();

        verify(stepperMotorMoverMock, times(10)).movePhi(phiStepper);
    }

    @Test
    public void setsHardwareCrashWhenPhiMovementFails() throws InvalidOperationException {
        daemon.maxIterations = 10;

        doThrow(new InvalidOperationException())
                .when(stepperMotorMoverMock)
                .movePhi(any());

        daemon.run();

        assertTrue(status().isHardwareCrash());
        assertEquals(1, daemon.currentIteration);
        verify(thetaStepper).destroy();
        verify(phiStepper).destroy();
    }
}
