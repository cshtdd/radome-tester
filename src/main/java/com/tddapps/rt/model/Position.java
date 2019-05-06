package com.tddapps.rt.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static com.tddapps.rt.utils.Round.ToPrecision;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder(toBuilder = true)
public class Position {
    private static final double MIN_THETA = 180.0;
    private static final double MAX_THETA = 360.0;

    private static final double MIN_PHI = 0.0;
    private static final double MAX_PHI = 180.0;


    private final double thetaDegrees;
    private final double phiDegrees;

    public boolean isValid() {
        var thetaIsValid = thetaDegrees >= MIN_THETA && thetaDegrees <= MAX_THETA;
        var phiIsValid = phiDegrees >= MIN_PHI && phiDegrees <= MAX_PHI;
        return thetaIsValid && phiIsValid;
    }

    public boolean almostEquals(Position that) {
        return WithinBounds(this.thetaDegrees - that.thetaDegrees) &&
                WithinBounds(this.phiDegrees - that.phiDegrees);
    }

    private boolean WithinBounds(double delta) {
        return ToPrecision(Math.abs(delta), 2) <= 0.01;
    }
}
