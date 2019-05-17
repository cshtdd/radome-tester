package com.tddapps.rt.model.internal;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.hardware.Delay;
import com.tddapps.rt.hardware.DelaySimulator;
import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;
import com.tddapps.rt.test.StatusRepositoryStub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PanningDaemonEventLoopTest {
    private final StatusRepository statusRepository = new StatusRepositoryStub();
    private final MovementService movementServiceMock = mock(MovementService.class);
    private final PanningDaemonEventLoopTestable daemon = new PanningDaemonEventLoopTestable(
            statusRepository, movementServiceMock, new DelaySimulator()
    );

    private Status status() {
        return statusRepository.CurrentStatus();
    }

    @Before
    public void Setup() {
        statusRepository.Save(Status.builder().build());
    }

    private static class PanningDaemonEventLoopTestable extends PanningDaemonEventLoop{
        public int MaxIterations = 1;
        public int CurrentIteration = 0;
        public RuntimeException seededException;

        private PanningDaemonEventLoopTestable(
                StatusRepository statusRepository,
                MovementService movementService,
                Delay delay
        ) {
            super(statusRepository, movementService, delay);
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

    @Test
    public void DoesNoActionWhenNotPanning() throws InvalidOperationException {
        daemon.run();

        assertFalse(status().isPanning());
        verify(movementServiceMock, never()).Move(any());
    }

    @Test
    public void MovesToTheStartPositionWhenPanning() throws InvalidOperationException {
        status().setPanning(true);

        daemon.run();

        assertTrue(status().isPanning());
        verify(movementServiceMock).Move(new Position(180, 0));
    }
}
