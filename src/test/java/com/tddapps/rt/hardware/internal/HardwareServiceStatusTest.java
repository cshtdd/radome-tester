package com.tddapps.rt.hardware.internal;

import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.DelaySimulator;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;
import com.tddapps.rt.test.StatusRepositoryStub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HardwareServiceStatusTest {
    private final StatusRepository statusRepository = new StatusRepositoryStub();

    private final HardwareServiceStatusTestable service = new HardwareServiceStatusTestable(
            statusRepository, new DelaySimulator()
    );

    @Before
    public void Setup() {
        statusRepository.Save(Status.builder().build());
    }

    private static class HardwareServiceStatusTestable extends HardwareServiceStatus {
        public int MaxIterations = 1;
        public int CurrentIteration = 0;

        public HardwareServiceStatusTestable(StatusRepository statusRepository, Delay delay) {
            super(statusRepository, delay);
        }

        @Override
        protected boolean RunCondition() {
            return CurrentIteration++ < MaxIterations;
        }
    }

    @Test
    public void RunFinishesBecauseItEvaluatesCondition() {
        service.MaxIterations = 10;

        service.run();

        assertEquals(11, service.CurrentIteration);
    }

    @Test
    public void RunWillNotDoAnythingIfHardwareHasAlreadyBeenInitialized() {
        statusRepository.CurrentStatus().setHardwareInitialized(true);

        service.run();

        assertEquals(0, service.CurrentIteration);
    }
}
