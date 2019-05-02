package com.tddapps.rt.mapping.internal;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.tddapps.rt.mapping.Mapper;

public class IocModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Mapper.class).to(AutoMapper.class).in(Singleton.class);
    }
}
