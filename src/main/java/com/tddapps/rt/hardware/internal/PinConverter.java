package com.tddapps.rt.hardware.internal;

import com.pi4j.io.gpio.Pin;

interface PinConverter {
    Pin fromBCMToGPIO(int bcmPin) throws IllegalArgumentException;
}
