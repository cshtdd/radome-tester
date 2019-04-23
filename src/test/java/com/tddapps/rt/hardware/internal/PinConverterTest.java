package com.tddapps.rt.hardware.internal;

import com.pi4j.io.gpio.RaspiPin;
import com.tddapps.rt.hardware.internal.pi.PinConverterPi3BPlus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PinConverterTest {
    @Test
    public void RaspberryPi3BPlus(){
        var converter = new PinConverterPi3BPlus();

        assertEquals(RaspiPin.GPIO_00, converter.BCMToGPIO(17));
        assertEquals(RaspiPin.GPIO_01, converter.BCMToGPIO(18));
        assertEquals(RaspiPin.GPIO_02, converter.BCMToGPIO(27));
        assertEquals(RaspiPin.GPIO_03, converter.BCMToGPIO(22));
        assertEquals(RaspiPin.GPIO_04, converter.BCMToGPIO(23));
        assertEquals(RaspiPin.GPIO_05, converter.BCMToGPIO(24));
        assertEquals(RaspiPin.GPIO_06, converter.BCMToGPIO(25));
        assertEquals(RaspiPin.GPIO_07, converter.BCMToGPIO(4));
        assertEquals(RaspiPin.GPIO_08, converter.BCMToGPIO(28));
        assertEquals(RaspiPin.GPIO_09, converter.BCMToGPIO(29));
        assertEquals(RaspiPin.GPIO_10, converter.BCMToGPIO(30));
        assertEquals(RaspiPin.GPIO_11, converter.BCMToGPIO(31));
    }
}
