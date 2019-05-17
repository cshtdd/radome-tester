package com.tddapps.rt.model.internal;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.model.PanningDaemon;
import com.tddapps.rt.model.StatusRepository;

public class IocModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MovementService.class).to(MovementServiceStatusChanger.class);
        bind(PanningSettingsRepository.class).to(PanningSettingsRepositoryFixed.class);
        bind(PanningDaemon.class).to(PanningDaemonEventLoop.class);

        bind(StatusRepository.class).to(StatusRepositoryInMemory.class).in(Singleton.class);
    }
}
