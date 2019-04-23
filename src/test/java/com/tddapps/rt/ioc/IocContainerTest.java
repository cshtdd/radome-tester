package com.tddapps.rt.ioc;

import com.tddapps.rt.model.SettingsReader;
import com.tddapps.rt.model.internal.SettingsReaderEnvironment;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IocContainerTest {

    @Test
    public void DependenciesAreNotSingletonByDefault(){
        var repository1 = IocContainer.getInstance().Resolve(SettingsReader.class);
        var repository2 = IocContainer.getInstance().Resolve(SettingsReader.class);

        assertFalse(repository1 == repository2);
    }

    @Test
    public void RegistersDependencies(){
        assertTrue(IocContainer.getInstance().Resolve(SettingsReader.class) instanceof SettingsReaderEnvironment);
    }
}
