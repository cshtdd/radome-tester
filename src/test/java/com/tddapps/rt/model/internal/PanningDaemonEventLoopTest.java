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
    private static final PanningSettings PANNING_SETTINGS = new PanningSettings(
            190, 210, 5,
            45, 85, 10
    );

    private final StatusRepository statusRepository = new StatusRepositoryStub();
    private final PanningSettingsRepositoryStub panningSettings = new PanningSettingsRepositoryStub();
    private final MovementService movementServiceMock = mock(MovementService.class);
    private final PanningDaemonEventLoopTestable daemon = new PanningDaemonEventLoopTestable(
            statusRepository, movementServiceMock, panningSettings, new DelaySimulator()
    );

    private Status status() {
        return statusRepository.CurrentStatus();
    }

    @Before
    public void Setup() {
        statusRepository.Save(Status.builder().build());
        panningSettings.settings = PANNING_SETTINGS;
    }

    private static class PanningDaemonEventLoopTestable extends PanningDaemonEventLoop{
        public int MaxIterations = 1;
        public int CurrentIteration = 0;
        public RuntimeException seededException;

        private PanningDaemonEventLoopTestable(
                StatusRepository statusRepository,
                MovementService movementService,
                PanningSettingsRepository settingsRepository,
                Delay delay
        ) {
            super(statusRepository, movementService, settingsRepository, delay);
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

        verify(movementServiceMock, never()).Move(any());
    }

    @Test
    public void DoesNoActionWhenCannotMove() throws InvalidOperationException {
        status().setPanning(true);
        when(movementServiceMock.CanMove(any())).thenReturn(false);

        daemon.run();

        verify(movementServiceMock, never()).Move(any());
    }

    @Test
    public void MovesToTheStartPositionWhenPanning() throws InvalidOperationException {
        status().setPanning(true);
        when(movementServiceMock.CanMove(new Position(190, 45))).thenReturn(true);

        daemon.run();

        assertTrue(status().isPanning());
        verify(movementServiceMock).Move(new Position(190, 45));
    }

    @Test
    public void MovesToMultiplePositionsThatMapTheEntireSurface() throws InvalidOperationException {
        daemon.MaxIterations = 100;
        status().setPanning(true);
        when(movementServiceMock.CanMove(any())).thenReturn(true);

        daemon.run();

        assertFalse(status().isPanning());
        double minTheta = PANNING_SETTINGS.getMinTheta();
        double maxTheta = PANNING_SETTINGS.getMaxTheta();
        double incrementTheta = PANNING_SETTINGS.getIncrementTheta();
        double minPhi = PANNING_SETTINGS.getMinPhi();
        double maxPhi = PANNING_SETTINGS.getMaxPhi();
        double incrementPhi = PANNING_SETTINGS.getIncrementPhi();
        for (double theta = minTheta; theta <= maxTheta; theta += incrementTheta){
            for (double phi = minPhi; phi <= maxPhi; phi += incrementPhi) {
                verify(movementServiceMock).Move(new Position(theta, phi));
            }
        }
    }
}
