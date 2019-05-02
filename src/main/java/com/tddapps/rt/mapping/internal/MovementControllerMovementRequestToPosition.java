package com.tddapps.rt.mapping.internal;

import com.tddapps.rt.api.controllers.MovementController;
import com.tddapps.rt.model.Position;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;

import static com.tddapps.rt.utils.Round.ToPrecision;

class MovementControllerMovementRequestToPosition implements ObjectFactory<Position> {
    @Override
    public Position create(Object source, MappingContext mappingContext) {
        var movementRequest = (MovementController.MovementRequest)source;

        var theta = ToPrecision(movementRequest.getTheta(), 2);
        var phi = ToPrecision(movementRequest.getPhi(), 2);

        return new Position(theta, phi);
    }
}
