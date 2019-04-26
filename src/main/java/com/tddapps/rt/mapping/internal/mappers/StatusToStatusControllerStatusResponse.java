package com.tddapps.rt.mapping.internal.mappers;

import com.tddapps.rt.api.controllers.StatusController;
import com.tddapps.rt.model.Status;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

import static com.tddapps.rt.utils.Round.ToPrecision;

public class StatusToStatusControllerStatusResponse extends CustomMapper<Status, StatusController.StatusResponse> {
    @Override
    public void mapAtoB(Status status, StatusController.StatusResponse statusResponse, MappingContext context) {
        super.mapAtoB(status, statusResponse, context);

        var currentPosition = status.getCurrentPosition();
        if (currentPosition != null){
            statusResponse.setDegreesTheta(ToPrecision(currentPosition.getThetaDegrees(), 2));
            statusResponse.setDegreesPhi(ToPrecision(currentPosition.getPhiDegrees(), 2));
        }
    }
}
