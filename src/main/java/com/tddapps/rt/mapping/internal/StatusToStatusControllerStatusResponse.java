package com.tddapps.rt.mapping.internal;

import com.tddapps.rt.api.controllers.StatusController;
import com.tddapps.rt.model.Status;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

import static com.tddapps.rt.utils.Round.ToPrecision;

class StatusToStatusControllerStatusResponse extends CustomMapper<Status, StatusController.StatusResponse> {
    @Override
    public void mapAtoB(Status status, StatusController.StatusResponse statusResponse, MappingContext context) {
        super.mapAtoB(status, statusResponse, context);

        var currentPosition = status.getCurrentPosition();
        if (currentPosition != null){
            statusResponse.setTheta(ToPrecision(currentPosition.getThetaDegrees(), 2));
            statusResponse.setPhi(ToPrecision(currentPosition.getPhiDegrees(), 2));
        }

        var commandedPosition = status.getCommandedPosition();
        if (commandedPosition != null){
            statusResponse.setCommandedTheta(ToPrecision(commandedPosition.getThetaDegrees(), 2));
            statusResponse.setCommandedPhi(ToPrecision(commandedPosition.getPhiDegrees(), 2));
        }
    }
}
