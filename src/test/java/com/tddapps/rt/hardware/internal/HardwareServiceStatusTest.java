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

public class HardwareServiceStatusTest {
    private final StatusRepository statusRepository = new StatusRepositoryStub();
    private final StepperMotorFactory stepperMotorFactoryMock = mock(StepperMotorFactory.class);
    private final CalibrationService calibrationServiceMock = mock(CalibrationService.class);

    private final HardwareServiceStatusTestable service = new HardwareServiceStatusTestable(
            statusRepository,
            new DelaySimulator(),
            stepperMotorFactoryMock,
            calibrationServiceMock
    );

    @Before
    public void Setup() {
        statusRepository.Save(Status.builder().build());
    }

    private static class HardwareServiceStatusTestable extends HardwareServiceStatus {
        public int MaxIterations = 1;
        public int CurrentIteration = 0;
        public RuntimeException seededException;

        public HardwareServiceStatusTestable(
                StatusRepository statusRepository,
                Delay delay,
                StepperMotorFactory stepperMotorFactory,
                CalibrationService calibrationServiceMock) {
            super(statusRepository, delay, stepperMotorFactory, calibrationServiceMock);
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
    }

    @Test
    public void CalibratesTheta() throws InvalidOperationException {
        var thetaStepper = mock(StepperMotor.class);
        when(stepperMotorFactoryMock.CreateTheta()).thenReturn(thetaStepper);

        service.run();

        verify(calibrationServiceMock).CalibrateThetaStepper(thetaStepper);
    }

    @Test
    public void SetsHardwareCrashWhenThetaCalibrationFails() throws InvalidOperationException {
        doThrow(new InvalidOperationException())
                .when(calibrationServiceMock)
                .CalibrateThetaStepper(any());

        service.run();

        assertFalse(statusRepository.CurrentStatus().isHardwareInitialized());
        assertTrue(statusRepository.CurrentStatus().isHardwareCrash());
    }
}
