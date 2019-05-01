package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Delay;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DelaySimulator implements Delay {
    @Override
    public void Wait(int ms) {
        log.debug(String.format("Sleeping; ms: %d", ms));
    }
}
