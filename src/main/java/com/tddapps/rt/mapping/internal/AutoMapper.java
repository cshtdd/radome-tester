package com.tddapps.rt.mapping.internal;

import com.tddapps.rt.api.controllers.StatusController;
import com.tddapps.rt.mapping.Mapper;
import com.tddapps.rt.mapping.internal.mappers.StatusToStatusControllerStatusResponse;
import com.tddapps.rt.model.Status;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class AutoMapper implements Mapper {
    private final MapperFactory mapperFactory;

    public AutoMapper(){
        mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(Status.class, StatusController.StatusResponse.class)
                .byDefault()
                .customize(new StatusToStatusControllerStatusResponse())
                .register();
    }

    @Override
    public <Source, Dest> Dest map(Source src, Class<Dest> type) {
        return mapperFactory.getMapperFacade().map(src, type);
    }
}
