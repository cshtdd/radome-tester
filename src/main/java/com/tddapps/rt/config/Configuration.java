package com.tddapps.rt.config;

import lombok.Data;

@Data
public class Configuration {
    private boolean isSimulation;
    private int[] thetaBcmPins;
    private int[] phiBcmPins;
}
