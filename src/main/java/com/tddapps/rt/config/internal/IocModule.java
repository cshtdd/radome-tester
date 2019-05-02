package com.tddapps.rt.config.internal;

import com.google.inject.AbstractModule;
import com.tddapps.rt.config.ConfigurationReader;

public class IocModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ConfigurationReaderJson.class);
        bind(ConfigurationReader.class).to(ConfigurationReaderSettings.class);
        bind(SettingsReader.class).to(SettingsReaderEnvironment.class);
    }
}
