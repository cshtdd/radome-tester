package com.tddapps.rt.hardware;

import com.tddapps.rt.InvalidOperationException;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
public class StepperMotorSimulator implements StepperMotor {
    private final String name;

    @Override
    public void Init() {
        log.info(ToLog("Init"));
    }

    @Override
    public void Destroy() {
        log.info(ToLog("Destroy"));
    }

    @Override
    public boolean MoveCW() {
        log.debug(ToLog("MoveCW"));
        return true;
    }

    @Override
    public boolean MoveCCW() throws InvalidOperationException {
        log.debug(ToLog("MoveCCW"));
        return true;
    }

    private String ToLog(String message){
        return String.format("%s; motor: %s", message, name);
    }
}
