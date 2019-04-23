package com.tddapps.rt.hardware.internal.pi;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.tddapps.rt.hardware.internal.PinConverter;

import java.util.HashMap;
import java.util.Map;

public class PinConverterPi3BPlus implements PinConverter {
    private final Map<Integer, Pin> mappings = new HashMap<>(){{
        put(17, RaspiPin.GPIO_00);
        put(18, RaspiPin.GPIO_01);
        put(27, RaspiPin.GPIO_02);
        put(22, RaspiPin.GPIO_03);
        put(23, RaspiPin.GPIO_04);
        put(24, RaspiPin.GPIO_05);
        put(25, RaspiPin.GPIO_06);
        put(4, RaspiPin.GPIO_07);
        put(28, RaspiPin.GPIO_08);
        put(29, RaspiPin.GPIO_09);
        put(30, RaspiPin.GPIO_10);
        put(31, RaspiPin.GPIO_11);
    }};

    public Pin BCMToGPIO(int bcmPin) throws IllegalArgumentException {
        if (!mappings.containsKey(bcmPin)){
            throw new IllegalArgumentException(String.format("BcmPin out of range; bcmPin=%d", bcmPin));
        }

        return mappings.get(bcmPin);
    }
}
