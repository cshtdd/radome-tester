package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Delay;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Sleep implements Delay {
    @Override
    public void Wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log.error("Thread Interrupted", e);
        }
    }
}
