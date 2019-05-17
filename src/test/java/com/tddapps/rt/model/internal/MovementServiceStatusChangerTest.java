package com.tddapps.rt.model.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.test.StatusRepositoryStub;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class MovementServiceStatusChangerTest {
    private final StatusRepositoryStub statusRepositoryStub = new StatusRepositoryStub();
    private final MovementService service = new MovementServiceStatusChanger(statusRepositoryStub);

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

    private Status status(){
        return statusRepositoryStub.read();
    }

    @Before
    public void setup(){
        statusRepositoryStub.save(DEFAULT_STATUS.toBuilder().build());
    }

    @Test
    public void defaultStatusValidation(){
        assertEquals(DEFAULT_STATUS, status());
        assertNotSame(DEFAULT_STATUS, status());
    }

    @Test
    public void canMoveWhenPanning(){
        status().setPanning(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        assertTrue(service.canMove(new Position(270, 90)));
    }

    @Test
    public void cannotMoveWhenIsAlreadyMoving(){
        status().setMoving(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        assertFalse(service.canMove(new Position(270, 90)));
    }

    @Test
    public void cannotMoveWhenCalibrating(){
        status().setCalibrating(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        assertFalse(service.canMove(new Position(270, 90)));
    }

    @Test
    public void cannotMoveWhenHardwareCrashed(){
        status().setHardwareCrash(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        assertFalse(service.canMove(new Position(270, 90)));
    }

    @Test
    public void cannotMoveWhenNotCalibrated(){
        status().setHardwareInitialized(true);

        assertFalse(service.canMove(new Position(270, 90)));
    }

    @Test
    public void cannotMoveWhenHardwareNotInitialized(){
        status().setCalibrated(true);

        assertFalse(service.canMove(new Position(270, 90)));
    }

    @Test
    public void cannotMoveWhenPositionIsInvalid(){
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        assertFalse(service.canMove(new Position(270, -90)));
    }

    @Test
    public void canMoveWhenPositionIsValidAndNoMovementIsInProgress(){
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        assertTrue(service.canMove(new Position(270, 90)));
    }

    @Test
    public void cannotPanWhenIsAlreadyPanning(){
        status().setPanning(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        assertFalse(service.canPan());
    }

    @Test
    public void cannotPanWhenIsAlreadyMoving(){
        status().setMoving(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        assertFalse(service.canPan());
    }

    @Test
    public void cannotPanWhenCalibrating(){
        status().setCalibrating(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        assertFalse(service.canPan());
    }

    @Test
    public void cannotPanWhenHardwareCrashed(){
        status().setHardwareCrash(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        assertFalse(service.canPan());
    }

    @Test
    public void cannotPanWhenNotCalibrated(){
        status().setHardwareInitialized(true);

        assertFalse(service.canPan());
    }

    @Test
    public void cannotPanWhenHardwareNotInitialized(){
        status().setCalibrated(true);

        assertFalse(service.canPan());
    }

    @Test
    public void canPanWhenProperlyInitializedAndNoMovementIsInProgress(){
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        assertTrue(service.canPan());
    }

    @Test
    public void stopChangesIsMovingToFalse(){
        status().setMoving(true);

        service.stop();

        assertFalse(status().isMoving());
    }

    @Test
    public void stopChangesIsPanningToFalse(){
        status().setPanning(true);

        service.stop();

        assertFalse(status().isPanning());
    }

    @Test
    public void moveDoesNotThrowWhenAlreadyPanning() throws InvalidOperationException {
        status().setPanning(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        service.move(new Position(270, 90));
    }

    @Test(expected = InvalidOperationException.class)
    public void moveThrowsWhenAlreadyMoving() throws InvalidOperationException {
        status().setMoving(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        service.move(new Position(270, 90));
    }

    @Test(expected = InvalidOperationException.class)
    public void moveThrowsWhenCalibrating() throws InvalidOperationException {
        status().setCalibrating(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        service.move(new Position(270, 90));
    }

    @Test(expected = InvalidOperationException.class)
    public void moveThrowsWhenHardwareCrashed() throws InvalidOperationException {
        status().setHardwareCrash(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        service.move(new Position(270, 90));
    }

    @Test(expected = InvalidOperationException.class)
    public void moveThrowsWhenNotCalibrated() throws InvalidOperationException {
        status().setHardwareInitialized(true);

        service.move(new Position(270, 90));
    }

    @Test(expected = InvalidOperationException.class)
    public void moveThrowsWhenHardwareNotInitialized() throws InvalidOperationException {
        status().setCalibrated(true);

        service.move(new Position(270, 90));
    }

    @Test(expected = InvalidOperationException.class)
    public void moveThrowsWhenPositionIsInvalid() throws InvalidOperationException {
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        service.move(new Position(-270, 90));
    }

    @Test
    public void moveChangesStatus() throws InvalidOperationException {
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        var expected = DEFAULT_STATUS.toBuilder()
                .isMoving(true)
                .isCalibrated(true)
                .isHardwareInitialized(true)
                .commandedPosition(new Position(270, 90))
                .build();

        service.move(new Position(270, 90));

        assertEquals(expected, status());
    }

    @Test(expected = InvalidOperationException.class)
    public void panThrowsWhenAlreadyPanning() throws InvalidOperationException {
        status().setPanning(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        service.pan();
    }

    @Test(expected = InvalidOperationException.class)
    public void panThrowsWhenAlreadyMoving() throws InvalidOperationException {
        status().setMoving(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        service.pan();
    }

    @Test(expected = InvalidOperationException.class)
    public void panThrowsWhenCalibrating() throws InvalidOperationException {
        status().setCalibrating(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        service.pan();
    }

    @Test(expected = InvalidOperationException.class)
    public void panThrowsWhenHardwareCrashed() throws InvalidOperationException {
        status().setHardwareCrash(true);
        status().setCalibrated(true);
        status().setHardwareInitialized(true);

        service.pan();
    }

    @Test(expected = InvalidOperationException.class)
    public void panThrowsWhenNotCalibrated() throws InvalidOperationException {
        status().setHardwareInitialized(true);

        service.pan();
    }

    @Test(expected = InvalidOperationException.class)
    public void panThrowsWhenHardwareNotInitialized() throws InvalidOperationException {
        status().setCalibrated(true);

        service.pan();
    }

    @Test
    public void panChangesStatus() throws InvalidOperationException {
        status().setHardwareInitialized(true);
        status().setCalibrated(true);

        var expected = DEFAULT_STATUS.toBuilder()
                .isCalibrated(true)
                .isHardwareInitialized(true)
                .isPanning(true)
                .build();

        service.pan();

        assertEquals(expected, status());
    }
}
