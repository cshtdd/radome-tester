package com.tddapps.rt.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Status {
    private int degreesTheta;
    private int degreesPhi;
    private boolean isMoving;
}
