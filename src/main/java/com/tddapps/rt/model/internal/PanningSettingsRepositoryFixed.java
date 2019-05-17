package com.tddapps.rt.model.internal;

import com.google.inject.Inject;
import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.model.Position;

public class PanningSettingsRepositoryFixed implements PanningSettingsRepository {
    private final ConfigurationReader configurationReader;

    @Inject
    public PanningSettingsRepositoryFixed(ConfigurationReader configurationReader) {
        this.configurationReader = configurationReader;
    }

    @Override
    public PanningSettings read() {
        var increment = readStepsIncrement();
        return new PanningSettings(
                Position.MIN_THETA, Position.MAX_THETA, increment,
                Position.MIN_PHI, Position.MAX_PHI, increment
        );
    }

    private double readStepsIncrement(){
        var configuration = configurationReader.read();

        if (configuration.isSimulation()){
            return 5.0;
        }

        return configuration.getPanningPrecision();
    }
}
