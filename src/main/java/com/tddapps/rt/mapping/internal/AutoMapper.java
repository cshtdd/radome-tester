package com.tddapps.rt.mapping.internal;

import com.tddapps.rt.api.controllers.MovementController;
import com.tddapps.rt.api.controllers.StatusController;
import com.tddapps.rt.mapping.Mapper;
import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.TypeFactory;

class AutoMapper implements Mapper {
    private final MapperFactory mapperFactory;

    public AutoMapper(){
        mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(Status.class, StatusController.StatusResponse.class)
                .byDefault()
                .customize(new CustomMapper<Status, StatusController.StatusResponse>() {
                    @Override
                    public void mapAtoB(Status status, StatusController.StatusResponse statusResponse, MappingContext context) {
                        super.mapAtoB(status, statusResponse, context);

                        statusResponse.setIsCalibrating(status.isCalibrating());
                        statusResponse.setIsCalibrated(status.isCalibrated());
                        statusResponse.setIsHardwareInitialized(status.isHardwareInitialized());
                        statusResponse.setIsHardwareCrash(status.isHardwareCrash());
                        statusResponse.setIsMoving(status.isMoving());
                        statusResponse.setIsPanning(status.isPanning());

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
                })
                .register();

        mapperFactory.registerObjectFactory(
                (source, mappingContext) -> {
                    var movementRequest = (MovementController.MovementStartRequest) source;

                    var theta = movementRequest.getTheta();
                    var phi = movementRequest.getPhi();

                    return new Position(theta, phi);
                },
                TypeFactory.valueOf(Position.class),
                TypeFactory.valueOf(MovementController.MovementStartRequest.class));
    }

    @Override
    public <Source, Dest> Dest map(Source src, Class<Dest> type) {
        return mapperFactory.getMapperFacade().map(src, type);
    }
}
