package com.tddapps.rt.ioc;

import com.tddapps.rt.ProgramStartup;
import com.tddapps.rt.api.ApiInitializer;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.HardwareInitializer;
import com.tddapps.rt.hardware.internal.PinConverter;
import com.tddapps.rt.hardware.internal.Sleep;
import com.tddapps.rt.hardware.internal.pi.PinConverterPi3BPlus;
import com.tddapps.rt.mapping.Mapper;
import com.tddapps.rt.mapping.internal.AutoMapper;
import com.tddapps.rt.model.ModelInitializer;
import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.model.SettingsReader;
import com.tddapps.rt.model.StatusRepository;
import com.tddapps.rt.model.internal.MovementServiceStatusChanger;
import com.tddapps.rt.model.internal.SettingsReaderEnvironment;
import com.tddapps.rt.model.internal.StatusRepositoryInMemory;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.PicoContainer;

import static org.picocontainer.Characteristics.CACHE;

public class IocContainer {
    private static final IocContainer sharedInstance = new IocContainer();

    private final PicoContainer resolver;

    public static IocContainer getInstance(){
        return sharedInstance;
    }

    private IocContainer(){
        resolver = RegisterBindings();
    }

    public <T> T Resolve(Class<T> type){
        return resolver.getComponent(type);
    }

    private PicoContainer RegisterBindings() {
        return new DefaultPicoContainer()
                .addComponent(ApiInitializer.class)
                .addComponent(HardwareInitializer.class)
                .addComponent(ModelInitializer.class)
                .addComponent(ProgramStartup.class)
                .addComponent(PinConverter.class, PinConverterPi3BPlus.class)
                .addComponent(SettingsReader.class, SettingsReaderEnvironment.class)
                .addComponent(MovementService.class, MovementServiceStatusChanger.class)
                .addComponent(Delay.class, Sleep.class)
                .as(CACHE).addComponent(Mapper.class, AutoMapper.class)
                .as(CACHE).addComponent(StatusRepository.class, StatusRepositoryInMemory.class);
    }
}
