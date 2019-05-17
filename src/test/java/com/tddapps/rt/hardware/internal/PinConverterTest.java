package com.tddapps.rt.hardware.internal;

import com.pi4j.io.gpio.RaspiPin;
import org.junit.Test;

import static org.junit.Assert.*;

public class PinConverterTest {
    private final PinConverter converterPi3BPlus = new PinConverterPi3BPlus();

    private void assertPi3BPlusConversionThrows(int bcmPin){
        IllegalArgumentException throwException = null;

        try {
            converterPi3BPlus.fromBCMToGPIO(bcmPin);
            fail(String.format("Should Have thrown for: %d", bcmPin));
        } catch (IllegalArgumentException e){
            throwException = e;
        }

        assertNotNull(throwException);
    }

    @Test
    public void raspberryPi3BPlus(){
        for (int i = -100; i <= 3; i++) {
            assertPi3BPlusConversionThrows(i);
        }

        assertEquals(RaspiPin.GPIO_07, converterPi3BPlus.fromBCMToGPIO(4));

        for (int i = 5; i <= 16; i++) {
            assertPi3BPlusConversionThrows(i);
        }

        assertEquals(RaspiPin.GPIO_00, converterPi3BPlus.fromBCMToGPIO(17));
        assertEquals(RaspiPin.GPIO_01, converterPi3BPlus.fromBCMToGPIO(18));

        for (int i = 19; i <= 21; i++) {
            assertPi3BPlusConversionThrows(i);
        }

        assertEquals(RaspiPin.GPIO_03, converterPi3BPlus.fromBCMToGPIO(22));
        assertEquals(RaspiPin.GPIO_04, converterPi3BPlus.fromBCMToGPIO(23));
        assertEquals(RaspiPin.GPIO_05, converterPi3BPlus.fromBCMToGPIO(24));
        assertEquals(RaspiPin.GPIO_06, converterPi3BPlus.fromBCMToGPIO(25));

        assertPi3BPlusConversionThrows(26);

        assertEquals(RaspiPin.GPIO_02, converterPi3BPlus.fromBCMToGPIO(27));
        assertEquals(RaspiPin.GPIO_08, converterPi3BPlus.fromBCMToGPIO(28));
        assertEquals(RaspiPin.GPIO_09, converterPi3BPlus.fromBCMToGPIO(29));
        assertEquals(RaspiPin.GPIO_10, converterPi3BPlus.fromBCMToGPIO(30));
        assertEquals(RaspiPin.GPIO_11, converterPi3BPlus.fromBCMToGPIO(31));

        for (int i = 32; i <= 100; i++) {
            assertPi3BPlusConversionThrows(i);
        }
    }
}
