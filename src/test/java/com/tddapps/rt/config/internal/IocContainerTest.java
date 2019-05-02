package com.tddapps.rt.config.internal;

import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.ioc.IocContainer;
import org.junit.Test;

import static org.junit.Assert.*;

public class IocContainerTest {
    @Test
    public void DependenciesAreNotSingletonByDefault(){
        var repository1 = IocContainer.getInstance().Resolve(SettingsReader.class);
        var repository2 = IocContainer.getInstance().Resolve(SettingsReader.class);

        assertNotSame(repository1, repository2);
    }

    @Test
    public void RegistersDependencies(){
        assertTrue(IocContainer.getInstance().Resolve(SettingsReader.class) instanceof SettingsReaderEnvironment);
        assertTrue(IocContainer.getInstance().Resolve(ConfigurationReader.class) instanceof ConfigurationReaderSettings);
    }
}
