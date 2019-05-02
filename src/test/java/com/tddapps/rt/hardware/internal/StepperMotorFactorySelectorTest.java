package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.config.Configuration;
import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.hardware.StepperMotor;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

public class StepperMotorFactorySelectorTest {
    private Configuration configuration = new Configuration();

    private final StepperMotor realTheta = mock(StepperMotor.class);
    private final StepperMotor realPhi = mock(StepperMotor.class);
    private final StepperMotor simulatorTheta = mock(StepperMotor.class);
    private final StepperMotor simulatorPhi = mock(StepperMotor.class);

    private StepperMotorFactory factory;

    @Before
    public void Setup() throws InvalidOperationException {
        var configurationReaderMock = mock(ConfigurationReader.class);
        when(configurationReaderMock.Read()).thenReturn(configuration);

        var realFactoryMock = mock(StepperMotorFactory.class);
        when(realFactoryMock.CreateTheta()).thenReturn(realTheta);
        when(realFactoryMock.CreatePhi()).thenReturn(realPhi);

        var simulatorFactoryMock = mock(StepperMotorFactory.class);
        when(simulatorFactoryMock.CreateTheta()).thenReturn(simulatorTheta);
        when(simulatorFactoryMock.CreatePhi()).thenReturn(simulatorPhi);

        factory = new StepperMotorFactorySelector(
                configurationReaderMock, realFactoryMock, simulatorFactoryMock
        );
    }

    @Test
    public void InvokesRealFactoryMethods() throws InvalidOperationException {
        assertEquals(realTheta, factory.CreateTheta());
        assertEquals(realPhi, factory.CreatePhi());
    }

    @Test
    public void InvokesSimulatorFactoryMethods() throws InvalidOperationException {
        configuration.setSimulation(true);

        assertEquals(simulatorTheta, factory.CreateTheta());
        assertEquals(simulatorPhi, factory.CreatePhi());
    }
}
