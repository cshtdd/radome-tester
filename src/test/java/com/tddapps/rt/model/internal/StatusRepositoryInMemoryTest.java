package com.tddapps.rt.model.internal;

import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class StatusRepositoryInMemoryTest {
    /*
    TODO make sure it is thread safe
    TODO make sure the returned status is a clone
     */

    private final StatusRepository repository = new StatusRepositoryInMemory();

    @Test
    public void DefaultStatusIsNull(){
        assertNull(repository.CurrentStatus());
    }

    @Test
    public void SavesStatus(){
        var seededFirst = new Status();
        seededFirst.setDegreesPhi(90);
        seededFirst.setMoving(false);
        repository.Save(seededFirst);

        var seededLast = new Status();
        seededLast.setDegreesPhi(100);
        seededLast.setMoving(true);
        repository.Save(seededLast);

        var readStatus = repository.CurrentStatus();

        assertEquals(seededLast, readStatus);
    }
}
