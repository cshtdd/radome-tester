package com.tddapps.rt.hardware;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DelaySimulator implements Delay {
    @Override
    public void waitMs(int ms) {
        log.debug(String.format("Sleeping; ms: %d", ms));
    }
}
