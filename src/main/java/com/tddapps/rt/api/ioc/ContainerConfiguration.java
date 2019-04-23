package com.tddapps.rt.api.ioc;

import com.tddapps.rt.ioc.IocContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContainerConfiguration {
    @Bean
    public IocContainer iocContainer() {
        return IocContainer.getInstance();
    }
}
