package com.tddapps.rt.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Status {
    private boolean isCalibrated;
    private boolean isCalibrating;
    private boolean isPanning;
    private boolean isMoving;
    private boolean isHardwareInitialized;
    private boolean isHardwareCrash;
    private Position currentPosition;
    private Position commandedPosition;

    public boolean canPan(){
        return !isPanning && canMove();
    }

    public boolean canMove(){
        return !isMoving &&
                !isCalibrating &&
                !isHardwareCrash &&
                isCalibrated &&
                isHardwareInitialized;
    }
}
