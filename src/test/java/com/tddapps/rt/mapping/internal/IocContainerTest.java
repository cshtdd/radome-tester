package com.tddapps.rt.mapping.internal;

import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.mapping.Mapper;
import org.junit.Test;

import static com.tddapps.rt.test.IocContainerHelper.assertSingleton;
import static junit.framework.TestCase.assertTrue;

public class IocContainerTest {
    @Test
    public void RegistersDependencies(){
        assertTrue(IocContainer.getInstance().Resolve(Mapper.class) instanceof AutoMapper);
    }

    @Test
    public void RegisterSingletons(){
        assertSingleton(Mapper.class);
    }
}
