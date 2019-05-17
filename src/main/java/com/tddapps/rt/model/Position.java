package com.tddapps.rt.model;

import com.tddapps.rt.utils.Round;
import lombok.Builder;
import lombok.Data;

import static com.tddapps.rt.utils.Round.AllowedError;

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
        this.thetaDegrees = Round(thetaDegrees);
        this.phiDegrees = Round(phiDegrees);
    }

    public boolean IsValid() {
        var thetaIsValid = thetaDegrees >= MIN_THETA && thetaDegrees <= MAX_THETA;
        var phiIsValid = phiDegrees >= MIN_PHI && phiDegrees <= MAX_PHI;
        return thetaIsValid && phiIsValid;
    }

    public boolean AlmostEquals(Position that) {
        return IsWithinBounds(this.thetaDegrees - that.thetaDegrees) &&
                IsWithinBounds(this.phiDegrees - that.phiDegrees);
    }

    private boolean IsWithinBounds(double delta) {
        return Round(Math.abs(delta)) <= AllowedError(DECIMAL_PLACES);
    }

    private static double Round(double value){
        return Round.ToPrecision(value, DECIMAL_PLACES);
    }
}
