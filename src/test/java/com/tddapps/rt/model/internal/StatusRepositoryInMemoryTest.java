package com.tddapps.rt.model.internal;

import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatusRepositoryInMemoryTest {
    private final StatusRepository repository = new StatusRepositoryInMemory();

    @Test
    public void defaultStatusIsNull(){
        assertNull(repository.read());
    }

    @Test
    public void savesStatus(){
        var seededFirst = Status.builder()
                .currentPosition(new Position(270, 90))
                .commandedPosition(new Position(270, 90))
                .isMoving(false)
                .build();
        repository.save(seededFirst);

        var seededLast = Status.builder()
                .currentPosition(new Position(270, 100))
                .commandedPosition(new Position(270, 90))
                .isMoving(true)
                .build();
        repository.save(seededLast);

        var readStatus = repository.read();

        assertEquals(seededLast, readStatus);
    }

    @Test
    public void updatesStatus(){
        var seededFirst = Status.builder()
                .currentPosition(new Position(270, 90))
                .commandedPosition(new Position(270, 90))
                .isMoving(false)
                .build();
        repository.save(seededFirst);

        repository.update(s -> s.toBuilder().isMoving(true).build());

        var actual = repository.read();
        var expected = Status.builder()
                .currentPosition(new Position(270, 90))
                .commandedPosition(new Position(270, 90))
                .isMoving(true)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void returnsACloneOfTheStatus(){
        var seeded = Status.builder()
                .currentPosition(new Position(270, 90))
                .commandedPosition(new Position(270, 90))
                .isMoving(true)
                .build();
        repository.save(seeded);

        var readStatus = repository.read();

        assertEquals(seeded, readStatus);
        assertNotSame(seeded, readStatus);
    }
}
