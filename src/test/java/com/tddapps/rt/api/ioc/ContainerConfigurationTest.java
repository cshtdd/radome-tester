package com.tddapps.rt.api.ioc;

import com.tddapps.rt.ioc.IocContainer;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ContainerConfigurationTest {
    private final ContainerConfiguration configuration = new ContainerConfiguration();

    @Test
    public void CanBuildTheIocContainer(){
        assertEquals(IocContainer.getInstance(), configuration.iocContainer());
    }
}
