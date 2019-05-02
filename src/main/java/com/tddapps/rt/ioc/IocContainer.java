package com.tddapps.rt.ioc;

import com.google.inject.*;
import com.tddapps.rt.ProgramStartup;
import com.tddapps.rt.StartupService;
import com.tddapps.rt.api.ApiInitializer;
import com.tddapps.rt.hardware.HardwareInitializer;
import com.tddapps.rt.mapping.Mapper;
import com.tddapps.rt.mapping.internal.AutoMapper;
import com.tddapps.rt.model.ModelInitializer;
import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.model.StatusRepository;
import com.tddapps.rt.model.internal.MovementServiceStatusChanger;
import com.tddapps.rt.model.internal.StatusRepositoryInMemory;

public class IocContainer {
    private static final IocContainer sharedInstance = new IocContainer();

    private final Injector resolver;

    public static IocContainer getInstance() {
        return sharedInstance;
    }

    private IocContainer() {
        resolver = RegisterBindings();
    }

    public <T> T Resolve(Class<T> type) {
        return resolver.getInstance(type);
    }

    private Injector RegisterBindings() {
        return Guice.createInjector(
                new com.tddapps.rt.config.internal.IocModule(),
                new com.tddapps.rt.hardware.internal.IocModule(),
                new DefaultModule()
        );
    }

    private static class DefaultModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(ApiInitializer.class);
            bind(ModelInitializer.class);
            bind(HardwareInitializer.class);

            bind(ProgramStartupFactory.class);
            bind(ProgramStartup.class).toProvider(ProgramStartupFactory.class);

            bind(MovementService.class).to(MovementServiceStatusChanger.class);

            bind(Mapper.class).to(AutoMapper.class).in(Singleton.class);
            bind(StatusRepository.class).to(StatusRepositoryInMemory.class).in(Singleton.class);
        }
    }

    public static class ProgramStartupFactory implements Provider<ProgramStartup> {
        private final ApiInitializer apiInitializer;
        private final ModelInitializer modelInitializer;
        private final HardwareInitializer hardwareInitializer;

        @Inject
        public ProgramStartupFactory(
                ApiInitializer apiInitializer,
                ModelInitializer modelInitializer,
                HardwareInitializer hardwareInitializer){
            this.apiInitializer = apiInitializer;
            this.modelInitializer = modelInitializer;
            this.hardwareInitializer = hardwareInitializer;
        }

        @Override
        public ProgramStartup get() {
            return new ProgramStartup(new StartupService[]{
                    apiInitializer,
                    modelInitializer,
                    hardwareInitializer
            });
        }
    }
}
