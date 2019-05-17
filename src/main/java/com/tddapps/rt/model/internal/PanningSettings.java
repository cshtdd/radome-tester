package com.tddapps.rt.model.internal;

import lombok.Data;

@Data
class PanningSettings {
    private final double minTheta;
    private final double maxTheta;
    private final double incrementTheta;

    private final double minPhi;
    private final double maxPhi;
    private final double incrementPhi;
}
