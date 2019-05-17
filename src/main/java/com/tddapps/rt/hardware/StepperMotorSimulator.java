package com.tddapps.rt.hardware;

import com.tddapps.rt.InvalidOperationException;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
public class StepperMotorSimulator implements StepperMotor {
    private final String name;

    @Override
    public void init() {
        log.info(toLog("Init"));
    }

    @Override
    public void destroy() {
        log.info(toLog("Destroy"));
    }

    @Override
    public boolean moveCW() {
        log.debug(toLog("MoveCW"));
        return true;
    }

    @Override
    public boolean moveCCW() throws InvalidOperationException {
        log.debug(toLog("MoveCCW"));
        return true;
    }

    private String toLog(String message){
        return String.format("%s; motor: %s", message, name);
    }
}
