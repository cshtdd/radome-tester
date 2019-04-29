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
            .isMoving(false)
            .isCalibrating(false)
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
    public void CannotMoveWhenIsAlreadyMoving(){
        status.setMoving(true);

        assertFalse(service.CanMove(new Position(270, 90)));
    }

    @Test
    public void CannotMoveWhenPositionIsInvalid(){
        assertFalse(service.CanMove(new Position(270, -90)));
    }

    @Test
    public void CanMoveWhenPositionIsValidAndNoMovementIsInProgress(){
        assertTrue(service.CanMove(new Position(270, 90)));
    }

    @Test
    public void StopChangesIsMovingToFalse(){
        status.setMoving(true);

        service.Stop();

        assertFalse(status.isMoving());
    }

    @Test(expected = InvalidOperationException.class)
    public void MoveThrowsWhenAlreadyMoving() throws InvalidOperationException {
        status.setMoving(true);

        service.Move(new Position(270, 90));
    }

    @Test(expected = InvalidOperationException.class)
    public void MoveThrowsWhenPositionIsInvalid() throws InvalidOperationException {
        service.Move(new Position(-270, 90));
    }

    @Test
    public void MoveChangesStatus() throws InvalidOperationException {
        var expected = DEFAULT_STATUS.toBuilder()
                .isMoving(true)
                .commandedPosition(new Position(270, 90))
                .build();

        service.Move(new Position(270, 90));

        assertEquals(expected, status);
    }
}
