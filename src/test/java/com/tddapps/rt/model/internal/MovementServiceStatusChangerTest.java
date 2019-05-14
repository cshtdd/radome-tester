package com.tddapps.rt.model.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MovementServiceStatusChangerTest {
    private final StatusRepository statusRepositoryMock = mock(StatusRepository.class);
    private final MovementService service = new MovementServiceStatusChanger(statusRepositoryMock);

    private final Status DEFAULT_STATUS = Status.builder()
            .isPanning(false)
            .isMoving(false)
            .isCalibrating(false)
            .isCalibrated(false)
            .isHardwareInitialized(false)
            .isHardwareCrash(false)
            .commandedPosition(new Position(0, 0))
            .currentPosition(new Position(180, 0))
            .build();

    private Status status = null;

    @Before
    public void Setup(){
        status = DEFAULT_STATUS.toBuilder().build();

        when(statusRepositoryMock.CurrentStatus()).thenReturn(status);
        doAnswer(i -> {
            status = i.getArgument(0, Status.class);
            return null;
        }).when(statusRepositoryMock).Save(any());
    }

    @Test
    public void DefaultStatusValidation(){
        assertEquals(DEFAULT_STATUS, status);
        assertNotSame(DEFAULT_STATUS, status);
    }

    @Test
    public void CannotMoveWhenIsAlreadyPanning(){
        status.setPanning(true);
        status.setCalibrated(true);
        status.setHardwareInitialized(true);

        assertFalse(service.CanMove(new Position(270, 90)));
    }

    @Test
    public void CannotMoveWhenIsAlreadyMoving(){
        status.setMoving(true);
        status.setCalibrated(true);
        status.setHardwareInitialized(true);

        assertFalse(service.CanMove(new Position(270, 90)));
    }

    @Test
    public void CannotMoveWhenCalibrating(){
        status.setCalibrating(true);
        status.setCalibrated(true);
        status.setHardwareInitialized(true);

        assertFalse(service.CanMove(new Position(270, 90)));
    }

    @Test
    public void CannotMoveWhenHardwareCrashed(){
        status.setHardwareCrash(true);
        status.setCalibrated(true);
        status.setHardwareInitialized(true);

        assertFalse(service.CanMove(new Position(270, 90)));
    }

    @Test
    public void CannotMoveWhenNotCalibrated(){
        status.setHardwareInitialized(true);

        assertFalse(service.CanMove(new Position(270, 90)));
    }

    @Test
    public void CannotMoveWhenHardwareNotInitialized(){
        status.setCalibrated(true);

        assertFalse(service.CanMove(new Position(270, 90)));
    }

    @Test
    public void CannotMoveWhenPositionIsInvalid(){
        status.setCalibrated(true);
        status.setHardwareInitialized(true);

        assertFalse(service.CanMove(new Position(270, -90)));
    }

    @Test
    public void CanMoveWhenPositionIsValidAndNoMovementIsInProgress(){
        status.setCalibrated(true);
        status.setHardwareInitialized(true);

        assertTrue(service.CanMove(new Position(270, 90)));
    }

    @Test
    public void StopChangesIsMovingToFalse(){
        status.setMoving(true);

        service.Stop();

        assertFalse(status.isMoving());
    }

    @Test
    public void StopChangesIsPanningToFalse(){
        status.setPanning(true);

        service.Stop();

        assertFalse(status.isPanning());
    }

    @Test(expected = InvalidOperationException.class)
    public void MoveThrowsWhenAlreadyPanning() throws InvalidOperationException {
        status.setPanning(true);
        status.setCalibrated(true);
        status.setHardwareInitialized(true);

        service.Move(new Position(270, 90));
    }

    @Test(expected = InvalidOperationException.class)
    public void MoveThrowsWhenAlreadyMoving() throws InvalidOperationException {
        status.setMoving(true);
        status.setCalibrated(true);
        status.setHardwareInitialized(true);

        service.Move(new Position(270, 90));
    }

    @Test(expected = InvalidOperationException.class)
    public void MoveThrowsWhenCalibrating() throws InvalidOperationException {
        status.setCalibrating(true);
        status.setCalibrated(true);
        status.setHardwareInitialized(true);

        service.Move(new Position(270, 90));
    }

    @Test(expected = InvalidOperationException.class)
    public void MoveThrowsWhenHardwareCrashed() throws InvalidOperationException {
        status.setHardwareCrash(true);
        status.setCalibrated(true);
        status.setHardwareInitialized(true);

        service.Move(new Position(270, 90));
    }

    @Test(expected = InvalidOperationException.class)
    public void MoveThrowsWhenNotCalibrated() throws InvalidOperationException {
        status.setHardwareInitialized(true);

        service.Move(new Position(270, 90));
    }

    @Test(expected = InvalidOperationException.class)
    public void MoveThrowsWhenHardwareNotInitialized() throws InvalidOperationException {
        status.setCalibrated(true);

        service.Move(new Position(270, 90));
    }

    @Test(expected = InvalidOperationException.class)
    public void MoveThrowsWhenPositionIsInvalid() throws InvalidOperationException {
        status.setCalibrated(true);
        status.setHardwareInitialized(true);

        service.Move(new Position(-270, 90));
    }

    @Test
    public void MoveChangesStatus() throws InvalidOperationException {
        status.setCalibrated(true);
        status.setHardwareInitialized(true);

        var expected = DEFAULT_STATUS.toBuilder()
                .isMoving(true)
                .isCalibrated(true)
                .isHardwareInitialized(true)
                .commandedPosition(new Position(270, 90))
                .build();

        service.Move(new Position(270, 90));

        assertEquals(expected, status);
    }
}
