package com.tddapps.rt.hardware.internal;

import com.pi4j.io.gpio.RaspiPin;
import com.tddapps.rt.hardware.internal.pi.PinConverterPi3BPlus;
import org.junit.Test;

import static org.junit.Assert.*;

public class PinConverterTest {
    private final PinConverter converterPi3BPlus = new PinConverterPi3BPlus();

    private void assertPi3BPlusConversionThrows(int bcmPin){
        IllegalArgumentException throwException = null;

        try {
            converterPi3BPlus.BCMToGPIO(bcmPin);
            fail(String.format("Should Have thrown for: %d", bcmPin));
        } catch (IllegalArgumentException e){
            throwException = e;
        }

        assertNotNull(throwException);
    }

    @Test
    public void RaspberryPi3BPlus(){
        for (int i = -100; i <= 3; i++) {
            assertPi3BPlusConversionThrows(i);
        }

        assertEquals(RaspiPin.GPIO_07, converterPi3BPlus.BCMToGPIO(4));

        for (int i = 5; i <= 16; i++) {
            assertPi3BPlusConversionThrows(i);
        }

        assertEquals(RaspiPin.GPIO_00, converterPi3BPlus.BCMToGPIO(17));
        assertEquals(RaspiPin.GPIO_01, converterPi3BPlus.BCMToGPIO(18));

        for (int i = 19; i <= 21; i++) {
            assertPi3BPlusConversionThrows(i);
        }

        assertEquals(RaspiPin.GPIO_03, converterPi3BPlus.BCMToGPIO(22));
        assertEquals(RaspiPin.GPIO_04, converterPi3BPlus.BCMToGPIO(23));
        assertEquals(RaspiPin.GPIO_05, converterPi3BPlus.BCMToGPIO(24));
        assertEquals(RaspiPin.GPIO_06, converterPi3BPlus.BCMToGPIO(25));

        assertPi3BPlusConversionThrows(26);

        assertEquals(RaspiPin.GPIO_02, converterPi3BPlus.BCMToGPIO(27));
        assertEquals(RaspiPin.GPIO_08, converterPi3BPlus.BCMToGPIO(28));
        assertEquals(RaspiPin.GPIO_09, converterPi3BPlus.BCMToGPIO(29));
        assertEquals(RaspiPin.GPIO_10, converterPi3BPlus.BCMToGPIO(30));
        assertEquals(RaspiPin.GPIO_11, converterPi3BPlus.BCMToGPIO(31));

        for (int i = 32; i <= 100; i++) {
            assertPi3BPlusConversionThrows(i);
        }
    }
}
