package com.tddapps.rt.mapping.internal;

import com.tddapps.rt.api.controllers.MovementController;
import com.tddapps.rt.api.controllers.StatusController;
import com.tddapps.rt.mapping.Mapper;
import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.TypeFactory;

class AutoMapper implements Mapper {
    private final MapperFactory mapperFactory;

    public AutoMapper(){
        mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(Status.class, StatusController.StatusResponse.class)
                .byDefault()
                .customize(new StatusToStatusControllerStatusResponse())
                .register();

        mapperFactory.registerObjectFactory(
                new MovementControllerMovementStartRequestToPosition(),
                TypeFactory.valueOf(Position.class),
                TypeFactory.valueOf(MovementController.MovementStartRequest.class));
    }

    @Override
    public <Source, Dest> Dest map(Source src, Class<Dest> type) {
        return mapperFactory.getMapperFacade().map(src, type);
    }
}
