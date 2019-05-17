package com.tddapps.rt.model.internal;

import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.DelaySimulator;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;
import com.tddapps.rt.test.StatusRepositoryStub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PanningDaemonEventLoopTest {
    private final StatusRepository statusRepository = new StatusRepositoryStub();
    private final PanningDaemonEventLoopTestable daemon = new PanningDaemonEventLoopTestable(
            statusRepository, new DelaySimulator()
    );

    @Before
    public void Setup() {
        statusRepository.Save(Status.builder().build());
    }

    private static class PanningDaemonEventLoopTestable extends PanningDaemonEventLoop{
        public int MaxIterations = 1;
        public int CurrentIteration = 0;
        public RuntimeException seededException;

        private PanningDaemonEventLoopTestable(StatusRepository statusRepository, Delay delay) {
            super(statusRepository, delay);
        }

        @Override
        protected boolean RunCondition() {
            if (seededException!=null){
                throw seededException;
            }

            return CurrentIteration++ < MaxIterations;
        }
    }

    @Test
    public void RunFinishesBecauseItEvaluatesCondition() {
        daemon.MaxIterations = 10;

        daemon.run();

        assertEquals(11, daemon.CurrentIteration);
    }
}
