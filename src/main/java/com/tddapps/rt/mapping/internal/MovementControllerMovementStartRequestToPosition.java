package com.tddapps.rt.mapping.internal;

import com.tddapps.rt.api.controllers.MovementController;
import com.tddapps.rt.model.Position;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;

class MovementControllerMovementStartRequestToPosition implements ObjectFactory<Position> {
    @Override
    public Position create(Object source, MappingContext mappingContext) {
        var movementRequest = (MovementController.MovementStartRequest)source;

        var theta = movementRequest.getTheta();
        var phi = movementRequest.getPhi();

        return new Position(theta, phi);
    }
}
