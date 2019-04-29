package com.tddapps.rt.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Status {
    private boolean isCalibrating;
    private boolean isMoving;
    private Position currentPosition;
    private Position commandedPosition;
}
