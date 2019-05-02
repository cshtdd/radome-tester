package com.tddapps.rt.test;

import com.tddapps.rt.ioc.IocContainer;

import static org.junit.Assert.assertSame;

public abstract class IocContainerHelper {
    public static <T> void assertSingleton(Class<T> type){
        var object1 = IocContainer.getInstance().Resolve(type);
        var object2 = IocContainer.getInstance().Resolve(type);

        assertSame(object1, object2);
    }
}