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
        panningSettings.settings = new PanningSettings(
                190, 210, 5,
                45, 85, 10
        );
    }

    private static class PanningDaemonEventLoopTestable extends PanningDaemonEventLoop{
        public int MaxIterations = 1;
        public int CurrentIteration = 0;
        public RuntimeException seededException;
        public Runnable conditionCallback = null;

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
            if (conditionCallback != null){
                conditionCallback.run();
            }

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
    public void MapsTheEntireSurface() throws InvalidOperationException {
        daemon.MaxIterations = 100;
        status().setPanning(true);
        when(movementServiceMock.CanMove(any())).thenReturn(true);
        panningSettings.settings = new PanningSettings(
                195, 210, 5,
                50, 85, 10
        );

        daemon.run();

        assertFalse(status().isPanning());
        for (double theta = 195; theta <= 210; theta += 5){
            for (double phi = 50; phi <= 85; phi += 10) {
                verify(movementServiceMock).Move(new Position(theta, phi));
            }
        }
    }

    @Test
    public void MapsTheEntireSurfaceEvenWithFractionalIncrements() throws InvalidOperationException {
        daemon.MaxIterations = 100;
        status().setPanning(true);
        when(movementServiceMock.CanMove(any())).thenReturn(true);
        panningSettings.settings = new PanningSettings(
                180, 180.5, 0.3,
                90, 92, 0.5
        );

        daemon.run();

        assertFalse(status().isPanning());
        verify(movementServiceMock).Move(new Position(180.0, 90.0));
        verify(movementServiceMock).Move(new Position(180.0, 90.5));
        verify(movementServiceMock).Move(new Position(180.0, 91.0));
        verify(movementServiceMock).Move(new Position(180.0, 91.5));
        verify(movementServiceMock).Move(new Position(180.0, 92.0));

        verify(movementServiceMock).Move(new Position(180.3, 90.0));
        verify(movementServiceMock).Move(new Position(180.3, 90.5));
        verify(movementServiceMock).Move(new Position(180.3, 91.0));
        verify(movementServiceMock).Move(new Position(180.3, 91.5));
        verify(movementServiceMock).Move(new Position(180.3, 92.0));
    }

    @Test
    public void MapsTheEntireSurfaceEvenWhenBoundariesAndIncrementsDoNotAlign() throws InvalidOperationException {
        daemon.MaxIterations = 100;
        status().setPanning(true);
        when(movementServiceMock.CanMove(any())).thenReturn(true);
        panningSettings.settings = new PanningSettings(
                180, 187, 3,
                90, 95, 2
        );

        daemon.run();

        assertFalse(status().isPanning());
        verify(movementServiceMock).Move(new Position(180, 90));
        verify(movementServiceMock).Move(new Position(180, 92));
        verify(movementServiceMock).Move(new Position(180, 94));
        verify(movementServiceMock).Move(new Position(183, 90));
        verify(movementServiceMock).Move(new Position(183, 92));
        verify(movementServiceMock).Move(new Position(183, 94));
        verify(movementServiceMock).Move(new Position(186, 90));
        verify(movementServiceMock).Move(new Position(186, 92));
        verify(movementServiceMock).Move(new Position(186, 94));
    }

    @Test
    public void StopsPanningWhenMovementIsInterrupted() throws InvalidOperationException {
        daemon.MaxIterations = 100;
        status().setPanning(true);
        when(movementServiceMock.CanMove(any())).thenReturn(true);
        panningSettings.settings = new PanningSettings(
                180, 190, 1,
                90, 90, 1
        );
        daemon.conditionCallback = () -> {
            if (daemon.CurrentIteration == 4){
                status().setPanning(false);
            }
        };

        daemon.run();

        assertFalse(status().isPanning());
        verify(movementServiceMock).Move(new Position(180.0, 90.0));
        verify(movementServiceMock).Move(new Position(181.0, 90.0));
        verify(movementServiceMock).Move(new Position(182.0, 90.0));
        verify(movementServiceMock).Move(new Position(183.0, 90.0));
    }
}
