package com.tddapps.rt.model.internal;

public class PanningSettingsRepositoryStub implements PanningSettingsRepository{
    public PanningSettings settings;

    @Override
    public PanningSettings Read() {
        return settings;
    }
}
