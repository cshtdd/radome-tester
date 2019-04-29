package com.tddapps.rt.mapping.internal.mappers;

import com.tddapps.rt.api.controllers.MovementController;
import com.tddapps.rt.model.Position;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

import static com.tddapps.rt.utils.Round.ToPrecision;

public class MovementControllerMovementRequestToPosition extends CustomMapper<MovementController.MovementRequest, Position> {
    @Override
    public void mapAtoB(MovementController.MovementRequest movementRequest, Position position, MappingContext context) {
        super.mapAtoB(movementRequest, position, context);

        var theta = ToPrecision(movementRequest.getThetaDegrees(), 2);
        position.setThetaDegrees(theta);

        var phi = ToPrecision(movementRequest.getPhiDegrees(), 2);
        position.setPhiDegrees(phi);
    }
}
