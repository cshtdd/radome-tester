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

public class HardwareDaemonEventLoopTest {
    private final StatusRepository statusRepository = new StatusRepositoryStub();
    private final StepperMotorFactory stepperMotorFactoryMock = mock(StepperMotorFactory.class);
    private final CalibrationService calibrationServiceMock = mock(CalibrationService.class);
    private final StepperMotorMover stepperMotorMoverMock = mock(StepperMotorMover.class);

    private final StepperMotor thetaStepper = mock(StepperMotor.class);
    private final StepperMotor phiStepper = mock(StepperMotor.class);

    private final HardwareDaemonEventLoopTestable service = new HardwareDaemonEventLoopTestable(
            statusRepository,
            new DelaySimulator(),
            stepperMotorFactoryMock,
            calibrationServiceMock,
            stepperMotorMoverMock
    );

    @Before
    public void Setup() throws InvalidOperationException {
        statusRepository.Save(Status.builder().build());

        when(stepperMotorFactoryMock.CreateTheta()).thenReturn(thetaStepper);
        when(stepperMotorFactoryMock.CreatePhi()).thenReturn(phiStepper);
    }

    private static class HardwareDaemonEventLoopTestable extends HardwareDaemonEventLoop {
        public int MaxIterations = 1;
        public int CurrentIteration = 0;
        public RuntimeException seededException;

        public HardwareDaemonEventLoopTestable(
                StatusRepository statusRepository,
                Delay delay,
                StepperMotorFactory stepperMotorFactory,
                CalibrationService calibrationServiceMock,
                StepperMotorMover stepperMotorMoverMock) {
            super(statusRepository, delay, stepperMotorFactory, calibrationServiceMock, stepperMotorMoverMock);
        }

        @Override
        protected boolean RunCondition() {
            if (seededException!=null){
                throw seededException;
            }

            return CurrentIteration++ < MaxIterations;
        }
    }

    @Test
    public void RunFinishesBecauseItEvaluatesCondition() {
        service.MaxIterations = 10;

        service.run();

        assertEquals(11, service.CurrentIteration);
    }

    @Test
    public void RunWillNotDoAnythingIfHardwareHasAlreadyBeenInitialized() {
        statusRepository.CurrentStatus().setHardwareInitialized(true);

        service.run();

        assertEquals(0, service.CurrentIteration);
    }

    @Test
    public void CreatesThetaMotor() throws InvalidOperationException {
        service.run();

        verify(stepperMotorFactoryMock).CreateTheta();
    }

    @Test
    public void InitializesThetaMotor() throws InvalidOperationException {
        service.run();

        verify(thetaStepper).Init();
    }

    @Test
    public void ThetaMotorCreationFailureChangesStatus() throws InvalidOperationException {
        when(stepperMotorFactoryMock.CreateTheta()).thenThrow(new InvalidOperationException());

        service.run();

        assertEquals(0, service.CurrentIteration);
        assertFalse(statusRepository.CurrentStatus().isHardwareInitialized());
        assertTrue(statusRepository.CurrentStatus().isHardwareCrash());
    }

    @Test
    public void CreatesPhiMotor() throws InvalidOperationException {
        service.run();

        verify(stepperMotorFactoryMock).CreatePhi();
    }

    @Test
    public void InitializesPhiMotor() throws InvalidOperationException {
        service.run();

        verify(phiStepper).Init();
    }

    @Test
    public void PhiMotorCreationFailureChangesStatus() throws InvalidOperationException {
        when(stepperMotorFactoryMock.CreatePhi()).thenThrow(new InvalidOperationException());

        service.run();

        assertEquals(0, service.CurrentIteration);
        assertFalse(statusRepository.CurrentStatus().isHardwareInitialized());
        assertTrue(statusRepository.CurrentStatus().isHardwareCrash());
    }

    @Test
    public void SetsHardwareInitializedStatus(){
        service.run();

        assertTrue(service.CurrentIteration > 0);
        assertTrue(statusRepository.CurrentStatus().isHardwareInitialized());
        assertFalse(statusRepository.CurrentStatus().isHardwareCrash());
    }

    @Test
    public void SetsHardwareCrashWhenUnexpectedErrorOccurs(){
        service.seededException = new RuntimeException();

        service.run();

        assertTrue(statusRepository.CurrentStatus().isHardwareInitialized());
        assertTrue(statusRepository.CurrentStatus().isHardwareCrash());
        verify(thetaStepper).Destroy();
        verify(phiStepper).Destroy();
    }

    @Test
    public void CalibratesTheta() throws InvalidOperationException {
        service.run();

        verify(calibrationServiceMock).CalibrateThetaStepper(thetaStepper);
        assertFalse(statusRepository.CurrentStatus().isCalibrating());
        assertTrue(statusRepository.CurrentStatus().isCalibrated());
    }

    @Test
    public void SetsHardwareCrashWhenThetaCalibrationFails() throws InvalidOperationException {
        doThrow(new InvalidOperationException())
                .when(calibrationServiceMock)
                .CalibrateThetaStepper(any());

        service.run();

        assertTrue(statusRepository.CurrentStatus().isCalibrating());
        assertFalse(statusRepository.CurrentStatus().isHardwareInitialized());
        assertTrue(statusRepository.CurrentStatus().isHardwareCrash());
        verify(thetaStepper).Destroy();
        verify(phiStepper).Destroy();
    }

    @Test
    public void CalibratesPhi() throws InvalidOperationException {
        service.run();

        verify(calibrationServiceMock).CalibratePhiStepper(phiStepper);
        assertFalse(statusRepository.CurrentStatus().isCalibrating());
        assertTrue(statusRepository.CurrentStatus().isCalibrated());
    }

    @Test
    public void SetsHardwareCrashWhenPhiCalibrationFails() throws InvalidOperationException {
        doThrow(new InvalidOperationException())
                .when(calibrationServiceMock)
                .CalibratePhiStepper(any());

        service.run();

        assertTrue(statusRepository.CurrentStatus().isCalibrating());
        assertFalse(statusRepository.CurrentStatus().isHardwareInitialized());
        assertTrue(statusRepository.CurrentStatus().isHardwareCrash());
        verify(thetaStepper).Destroy();
        verify(phiStepper).Destroy();
    }

    @Test
    public void MovesThetaOnTheLoop() throws InvalidOperationException {
        service.MaxIterations = 10;

        service.run();

        verify(stepperMotorMoverMock, times(10)).MoveTheta(thetaStepper);
    }

    @Test
    public void SetsHardwareCrashWhenThetaMovementFails() throws InvalidOperationException {
        service.MaxIterations = 10;

        doThrow(new InvalidOperationException())
                .when(stepperMotorMoverMock)
                .MoveTheta(any());

        service.run();

        assertTrue(statusRepository.CurrentStatus().isHardwareCrash());
        assertEquals(1, service.CurrentIteration);
        verify(thetaStepper).Destroy();
        verify(phiStepper).Destroy();
    }

    @Test
    public void MovesPhiOnTheLoop() throws InvalidOperationException {
        service.MaxIterations = 10;

        service.run();

        verify(stepperMotorMoverMock, times(10)).MovePhi(phiStepper);
    }

    @Test
    public void SetsHardwareCrashWhenPhiMovementFails() throws InvalidOperationException {
        service.MaxIterations = 10;

        doThrow(new InvalidOperationException())
                .when(stepperMotorMoverMock)
                .MovePhi(any());

        service.run();

        assertTrue(statusRepository.CurrentStatus().isHardwareCrash());
        assertEquals(1, service.CurrentIteration);
        verify(thetaStepper).Destroy();
        verify(phiStepper).Destroy();
    }
}
