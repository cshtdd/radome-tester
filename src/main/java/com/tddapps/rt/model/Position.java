package com.tddapps.rt.model;

import lombok.Data;

@Data
public class Position {
    private final double thetaDegrees;
    private final double phiDegrees;

    public boolean isValid() {
        return (thetaDegrees >= 180 && thetaDegrees <= 360) &&
                (phiDegrees >= 0 && phiDegrees <= 180);
    }
}
