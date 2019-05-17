package com.tddapps.rt.model.internal;

import com.tddapps.rt.model.Position;

public class PanningSettingsRepositoryFixed implements PanningSettingsRepository {
    @Override
    public PanningSettings Read() {
        return new PanningSettings(
                Position.MIN_THETA, Position.MAX_THETA, 0.1,
                Position.MIN_PHI, Position.MAX_PHI, 0.1
        );
    }
}
