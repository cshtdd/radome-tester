package com.tddapps.rt.mapping;

import com.tddapps.rt.api.controllers.MovementController;
import com.tddapps.rt.api.controllers.StatusController;
import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MappingTest {
    private final Mapper mapper = IocContainer.getInstance().resolve(Mapper.class);

    @Test
    public void statusToStatusControllerStatusResponse(){
        var expected = new StatusController.StatusResponse();
        expected.setIsCalibrating(true);
        expected.setIsCalibrated(true);
        expected.setIsHardwareInitialized(true);
        expected.setIsHardwareCrash(true);
        expected.setIsMoving(true);
        expected.setIsPanning(true);
        expected.setTheta(102.25);
        expected.setPhi(103.23);
        expected.setCommandedTheta(100.21);
        expected.setCommandedPhi(101.33);

        var status = Status.builder()
                .currentPosition(new Position(102.25333, 103.2333))
                .commandedPosition(new Position(100.212222, 101.33333))
                .isPanning(true)
                .isMoving(true)
                .isCalibrating(true)
                .isCalibrated(true)
                .isHardwareInitialized(true)
                .isHardwareCrash(true)
                .build();

        var actual = mapper.map(status, StatusController.StatusResponse.class);

        assertEquals(expected, actual);
    }

    @Test
    public void movementControllerMovementRequestToPosition(){
        var expected = new Position(193.45, 45.33);

        var request = new MovementController.MovementStartRequest(193.446, 45.33333);

        var actual = mapper.map(request, Position.class);

        assertEquals(expected, actual);
    }
}
