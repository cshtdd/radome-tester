package com.tddapps.rt.mapping;

import com.tddapps.rt.api.controllers.StatusController;
import com.tddapps.rt.mapping.internal.AutoMapper;
import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MappingTest {
    private final Mapper mapper = new AutoMapper();

    @Test
    public void StatusToStatusControllerStatusResponse(){
        var expected = new StatusController.StatusResponse();
        expected.setCalibrating(true);
        expected.setMoving(true);
        expected.setTheta(102.25);
        expected.setPhi(103.23);
        expected.setCommandedTheta(100.21);
        expected.setCommandedPhi(101.33);

        var status = Status.builder()
                .currentPosition(new Position(102.25333, 103.2333))
                .commandedPosition(new Position(100.212222, 101.33333))
                .isMoving(true)
                .isCalibrating(true)
                .build();

        var actual = mapper.map(status, StatusController.StatusResponse.class);

        assertEquals(expected, actual);
    }
}
