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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PanningDaemonEventLoopTest {
    private final List<double[]> movements = new ArrayList<>();

    private final StatusRepository statusRepository = new StatusRepositoryStub();
    private final PanningSettingsRepositoryStub panningSettings = new PanningSettingsRepositoryStub();
    private final MovementService movementServiceMock = mock(MovementService.class);
    private final PanningDaemonEventLoopTestable daemon = new PanningDaemonEventLoopTestable(
            statusRepository, movementServiceMock, panningSettings, new DelaySimulator()
    );

    private Status status() {
        return statusRepository.read();
    }

    @Before
    public void setup() throws InvalidOperationException {
        statusRepository.save(Status.builder().build());
        panningSettings.settings = new PanningSettings(
                190, 210, 5,
                45, 85, 10
        );

        doAnswer(i -> {
            var p = i.getArgument(0, Position.class);
            movements.add(new double[]{p.getThetaDegrees(), p.getPhiDegrees()});
            return null;
        }).when(movementServiceMock).move(any());
    }

    private static class PanningDaemonEventLoopTestable extends PanningDaemonEventLoop {
        public int maxIterations = 1;
        public int currentIteration = 0;
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
        protected boolean runCondition() {
            if (conditionCallback != null) {
                conditionCallback.run();
            }

            if (seededException != null) {
                throw seededException;
            }

            return currentIteration++ < maxIterations;
        }
    }

    @Test
    public void runFinishesBecauseItEvaluatesCondition() {
        daemon.maxIterations = 10;

        daemon.run();

        assertEquals(11, daemon.currentIteration);
    }

    @Test
    public void doesNoActionWhenNotPanning() throws InvalidOperationException {
        daemon.run();

        verify(movementServiceMock, never()).move(any());
    }

    @Test
    public void doesNoActionWhenCannotMove() throws InvalidOperationException {
        status().setPanning(true);
        when(movementServiceMock.canMove(any())).thenReturn(false);

        daemon.run();

        verify(movementServiceMock, never()).move(any());
    }

    @Test
    public void movesToTheStartPositionWhenPanning() throws InvalidOperationException {
        status().setPanning(true);
        when(movementServiceMock.canMove(new Position(190, 45))).thenReturn(true);

        daemon.run();

        assertTrue(status().isPanning());
        verify(movementServiceMock).move(new Position(190, 45));
    }

    @Test
    public void setsHardwareCrashWhenMovementFails() throws InvalidOperationException {
        status().setPanning(true);
        when(movementServiceMock.canMove(new Position(190, 45))).thenReturn(true);
        doThrow(new InvalidOperationException()).when(movementServiceMock).move(any());

        daemon.run();

        assertTrue(status().isHardwareCrash());
    }

    @Test
    public void mapsTheEntireSurface() throws InvalidOperationException {
        daemon.maxIterations = 100;
        status().setPanning(true);
        when(movementServiceMock.canMove(any())).thenReturn(true);
        panningSettings.settings = new PanningSettings(
                195, 210, 5,
                50, 85, 10
        );

        daemon.run();

        assertFalse(status().isPanning());
        var expected = new ArrayList<double[]>();
        for (double theta = 195; theta <= 210; theta += 5) {
            for (double phi = 50; phi <= 85; phi += 10) {
                expected.add(new double[]{ theta, phi });
            }
        }
        assertArrayEquals(expected.toArray(), movements.toArray());
    }

    @Test
    public void mapsTheEntireSurfaceEvenWithFractionalIncrements() throws InvalidOperationException {
        daemon.maxIterations = 100;
        status().setPanning(true);
        when(movementServiceMock.canMove(any())).thenReturn(true);
        panningSettings.settings = new PanningSettings(
                180, 180.5, 0.3,
                90, 92, 0.5
        );

        daemon.run();

        assertFalse(status().isPanning());
        var expected = new double[][]{
            new double[] { 180.0, 90.0 },
            new double[] { 180.0, 90.5 },
            new double[] { 180.0, 91.0 },
            new double[] { 180.0, 91.5 },
            new double[] { 180.0, 92.0 },

            new double[] { 180.3, 90.0 },
            new double[] { 180.3, 90.5 },
            new double[] { 180.3, 91.0 },
            new double[] { 180.3, 91.5 },
            new double[] { 180.3, 92.0 },
        };
        assertArrayEquals(expected, movements.toArray());
    }

    @Test
    public void mapsTheEntireSurfaceEvenWhenBoundariesAndIncrementsDoNotAlign() throws InvalidOperationException {
        daemon.maxIterations = 100;
        status().setPanning(true);
        when(movementServiceMock.canMove(any())).thenReturn(true);
        panningSettings.settings = new PanningSettings(
                180, 187, 3,
                90, 95, 2
        );

        daemon.run();

        assertFalse(status().isPanning());
        var expected = new double[][]{
                new double[] { 180, 90 },
                new double[] { 180, 92 },
                new double[] { 180, 94 },
                new double[] { 183, 90 },
                new double[] { 183, 92 },
                new double[] { 183, 94 },
                new double[] { 186, 90 },
                new double[] { 186, 92 },
                new double[] { 186, 94 },
        };
        assertArrayEquals(expected, movements.toArray());
    }

    @Test
    public void stopsPanningWhenMovementIsInterrupted() throws InvalidOperationException {
        daemon.maxIterations = 100;
        status().setPanning(true);
        when(movementServiceMock.canMove(any())).thenReturn(true);
        panningSettings.settings = new PanningSettings(
                180, 190, 1,
                90, 90, 1
        );
        daemon.conditionCallback = () -> {
            if (daemon.currentIteration == 4) {
                status().setPanning(false);
            }
        };

        daemon.run();

        assertFalse(status().isPanning());
        var expected = new double[][]{
            new double[] { 180.0, 90.0 },
            new double[] { 181.0, 90.0 },
            new double[] { 182.0, 90.0 },
            new double[] { 183.0, 90.0 },
        };
        assertArrayEquals(expected, movements.toArray());
    }

    @Test
    public void RestartsPanningFromTheBeginning() throws InvalidOperationException {
        daemon.maxIterations = 100;
        status().setPanning(true);
        when(movementServiceMock.canMove(any())).thenReturn(true);
        panningSettings.settings = new PanningSettings(
                180, 190, 1,
                90, 90, 1
        );
        Runnable resumeCallback = () -> {
            if (daemon.currentIteration == 7) {
                status().setPanning(true);
                daemon.conditionCallback = null;
            }
        };
        Runnable haltCallback = () -> {
            if (daemon.currentIteration == 4) {
                status().setPanning(false);
                daemon.conditionCallback = resumeCallback;
            }
        };
        daemon.conditionCallback = haltCallback;

        daemon.run();

        assertFalse(status().isPanning());
        var expected = new double[][]{
                new double[] { 180.0, 90.0 },
                new double[] { 181.0, 90.0 },
                new double[] { 182.0, 90.0 },
                new double[] { 183.0, 90.0 },

                new double[] { 180.0, 90.0 },
                new double[] { 181.0, 90.0 },
                new double[] { 182.0, 90.0 },
                new double[] { 183.0, 90.0 },
                new double[] { 184.0, 90.0 },
                new double[] { 185.0, 90.0 },
                new double[] { 186.0, 90.0 },
                new double[] { 187.0, 90.0 },
                new double[] { 188.0, 90.0 },
                new double[] { 189.0, 90.0 },
                new double[] { 190.0, 90.0 },
        };
        assertArrayEquals(expected, movements.toArray());
    }
}
