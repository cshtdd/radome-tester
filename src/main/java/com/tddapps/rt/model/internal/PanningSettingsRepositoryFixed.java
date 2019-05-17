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
    public PanningSettings Read() {
        return new PanningSettings(
                Position.MIN_THETA, Position.MAX_THETA, StepsIncrement(),
                Position.MIN_PHI, Position.MAX_PHI, StepsIncrement()
        );
    }

    private double StepsIncrement(){
        if (configurationReader.Read().isSimulation()){
            return 5.0;
        }

        return 0.1;
    }
}
