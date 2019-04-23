package com.tddapps.rt.hardware.internal.pi;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.tddapps.rt.hardware.internal.PinConverter;

public class PinConverterPi3BPlus implements PinConverter {
    public Pin BCMToGPIO(int bcmPin){
        return RaspiPin.GPIO_00;
    }
}
