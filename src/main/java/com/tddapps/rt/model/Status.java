package com.tddapps.rt.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Status {
    private boolean isCalibrated;
    private boolean isCalibrating;
    private boolean isMoving;
    private boolean isHardwareInitialized;
    private boolean isHardwareCrash;
    private Position currentPosition;
    private Position commandedPosition;
}
