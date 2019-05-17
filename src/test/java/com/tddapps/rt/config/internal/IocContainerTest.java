package com.tddapps.rt.config.internal;

import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.ioc.IocContainer;
import org.junit.Test;

import static org.junit.Assert.*;

public class IocContainerTest {
    @Test
    public void dependenciesAreNotSingletonByDefault(){
        var repository1 = IocContainer.getInstance().resolve(SettingsReader.class);
        var repository2 = IocContainer.getInstance().resolve(SettingsReader.class);

        assertNotSame(repository1, repository2);
    }

    @Test
    public void registersDependencies(){
        assertTrue(IocContainer.getInstance().resolve(SettingsReader.class) instanceof SettingsReaderEnvironment);
        assertTrue(IocContainer.getInstance().resolve(ConfigurationReader.class) instanceof ConfigurationReaderSettings);
    }
}
