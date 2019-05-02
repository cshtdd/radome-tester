package com.tddapps.rt.mapping.internal;

import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.mapping.Mapper;
import static org.mockito.Mockito.when;

public abstract class IocContainerAutoMappingHelper {
    public static void SetupAutoMapping(IocContainer iocContainerMock){
        when(iocContainerMock.Resolve(Mapper.class))
                .thenReturn(new AutoMapper());
    }
}