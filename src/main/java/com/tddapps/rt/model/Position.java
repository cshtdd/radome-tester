package com.tddapps.rt.model;

import com.tddapps.rt.utils.Round;
import lombok.Builder;
import lombok.Data;

import static com.tddapps.rt.utils.Round.allowedError;

@Data
@Builder(toBuilder = true)
public class Position {
    private static final int DECIMAL_PLACES = 2;

    public static final double MIN_THETA = 180.0;
    public static final double MAX_THETA = 360.0;

    public static final double MIN_PHI = 0.0;
    public static final double MAX_PHI = 180.0;


    private final double thetaDegrees;
    private final double phiDegrees;

    public Position(double thetaDegrees, double phiDegrees){
        this.thetaDegrees = round(thetaDegrees);
        this.phiDegrees = round(phiDegrees);
    }

    public boolean isValid() {
        var thetaIsValid = thetaDegrees >= MIN_THETA && thetaDegrees <= MAX_THETA;
        var phiIsValid = phiDegrees >= MIN_PHI && phiDegrees <= MAX_PHI;
        return thetaIsValid && phiIsValid;
    }

    public boolean almostEquals(Position that) {
        return isWithinBounds(this.thetaDegrees - that.thetaDegrees) &&
                isWithinBounds(this.phiDegrees - that.phiDegrees);
    }

    private boolean isWithinBounds(double delta) {
        return round(Math.abs(delta)) <= allowedError(DECIMAL_PLACES);
    }

    private static double round(double value){
        return Round.toPrecision(value, DECIMAL_PLACES);
    }
}
