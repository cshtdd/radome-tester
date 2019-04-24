package com.tddapps.rt.model;

import lombok.Data;

@Data
public class Status {
    private int degreesTheta;
    private int degreesPhi;
    private boolean isMoving;
}
