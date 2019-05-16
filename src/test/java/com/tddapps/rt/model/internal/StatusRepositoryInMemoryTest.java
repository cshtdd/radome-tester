package com.tddapps.rt.model.internal;

import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatusRepositoryInMemoryTest {
    private final StatusRepository repository = new StatusRepositoryInMemory();

    @Test
    public void DefaultStatusIsNull(){
        assertNull(repository.CurrentStatus());
    }

    @Test
    public void SavesStatus(){
        var seededFirst = Status.builder()
                .currentPosition(new Position(270, 90))
                .commandedPosition(new Position(270, 90))
                .isMoving(false)
                .build();
        repository.Save(seededFirst);

        var seededLast = Status.builder()
                .currentPosition(new Position(270, 100))
                .commandedPosition(new Position(270, 90))
                .isMoving(true)
                .build();
        repository.Save(seededLast);

        var readStatus = repository.CurrentStatus();

        assertEquals(seededLast, readStatus);
    }

    @Test
    public void UpdatesStatus(){
        var seededFirst = Status.builder()
                .currentPosition(new Position(270, 90))
                .commandedPosition(new Position(270, 90))
                .isMoving(false)
                .build();
        repository.Save(seededFirst);

        repository.Update(s -> s.toBuilder().isMoving(true).build());

        var actual = repository.CurrentStatus();
        var expected = Status.builder()
                .currentPosition(new Position(270, 90))
                .commandedPosition(new Position(270, 90))
                .isMoving(true)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void ReturnsACloneOfTheStatus(){
        var seeded = Status.builder()
                .currentPosition(new Position(270, 90))
                .commandedPosition(new Position(270, 90))
                .isMoving(true)
                .build();
        repository.Save(seeded);

        var readStatus = repository.CurrentStatus();

        assertEquals(seeded, readStatus);
        assertNotSame(seeded, readStatus);
    }
}
