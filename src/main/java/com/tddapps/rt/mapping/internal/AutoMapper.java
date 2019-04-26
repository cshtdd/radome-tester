package com.tddapps.rt.mapping.internal;

import com.tddapps.rt.mapping.Mapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class AutoMapper implements Mapper {
    private final MapperFactory mapperFactory;

    public AutoMapper(){
        mapperFactory = new DefaultMapperFactory.Builder().build();
    }

    @Override
    public <Source, Dest> Dest map(Source src, Class<Dest> type) {
        return mapperFactory.getMapperFacade().map(src, type);
    }
}
