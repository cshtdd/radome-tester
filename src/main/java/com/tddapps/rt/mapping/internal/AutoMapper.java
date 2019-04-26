package com.tddapps.rt.mapping.internal;

import com.tddapps.rt.api.controllers.StatusController;
import com.tddapps.rt.mapping.Mapper;
import com.tddapps.rt.model.Status;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import static com.tddapps.rt.utils.Round.ToPrecision;

public class AutoMapper implements Mapper {
    private final MapperFactory mapperFactory;

    public AutoMapper(){
        mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(Status.class, StatusController.StatusResponse.class)
                .byDefault()
                .customize(new CustomMapper<Status, StatusController.StatusResponse>() {
                    @Override
                    public void mapAtoB(Status status, StatusController.StatusResponse statusResponse, MappingContext context) {
                        super.mapAtoB(status, statusResponse, context);

                        var currentPosition = status.getCurrentPosition();
                        if (currentPosition != null){
                            statusResponse.setDegreesTheta(ToPrecision(currentPosition.getThetaDegrees(), 2));
                            statusResponse.setDegreesPhi(ToPrecision(currentPosition.getPhiDegrees(), 2));
                        }
                    }
                })
                .register();
    }

    @Override
    public <Source, Dest> Dest map(Source src, Class<Dest> type) {
        return mapperFactory.getMapperFacade().map(src, type);
    }
}
