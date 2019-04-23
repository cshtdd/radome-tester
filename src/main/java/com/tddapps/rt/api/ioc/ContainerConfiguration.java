package com.tddapps.rt.api.ioc;

import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.model.SettingsReader;
import com.tddapps.rt.model.internal.SettingsReaderEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContainerConfiguration {
    @Bean
    public SettingsReader settingsReader(){
        return new SettingsReaderEnvironment();
    }

    @Bean
    public IocContainer iocContainer() {
        return IocContainer.getInstance();
    }
}
