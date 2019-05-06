package com.tddapps.rt.mapping.internal;

import com.tddapps.rt.api.controllers.StatusController;
import com.tddapps.rt.model.Status;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

class StatusToStatusControllerStatusResponse extends CustomMapper<Status, StatusController.StatusResponse> {
    @Override
    public void mapAtoB(Status status, StatusController.StatusResponse statusResponse, MappingContext context) {
        super.mapAtoB(status, statusResponse, context);

        var currentPosition = status.getCurrentPosition();
        if (currentPosition != null){
            statusResponse.setTheta(currentPosition.getThetaDegrees());
            statusResponse.setPhi(currentPosition.getPhiDegrees());
        }

        var commandedPosition = status.getCommandedPosition();
        if (commandedPosition != null){
            statusResponse.setCommandedTheta(commandedPosition.getThetaDegrees());
            statusResponse.setCommandedPhi(commandedPosition.getPhiDegrees());
        }
    }
}
