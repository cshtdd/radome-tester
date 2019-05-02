package com.tddapps.rt.ioc;

import com.tddapps.rt.ProgramStartup;
import com.tddapps.rt.api.ApiInitializer;
import com.tddapps.rt.hardware.HardwareInitializer;
import com.tddapps.rt.model.ModelInitializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IocContainerTest {
    @Test
    public void RegistersStartupServices(){
        var program = IocContainer.getInstance().Resolve(ProgramStartup.class);

        assertEquals(3, program.services.length);
        assertTrue(program.services[0] instanceof ApiInitializer);
        assertTrue(program.services[1] instanceof ModelInitializer);
        assertTrue(program.services[2] instanceof HardwareInitializer);
    }
}
