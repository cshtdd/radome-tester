package com.tddapps.rt.hardware.internal;

import com.pi4j.io.gpio.Pin;

public interface PinConverter {
    Pin BCMToGPIO(int bcmPin) throws IllegalArgumentException;
}
