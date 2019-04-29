package com.tddapps.rt.model.internal;

import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MovementServiceStatusChangerTest {
    private final StatusRepository statusRepositoryMock = mock(StatusRepository.class);
    private final MovementService service = new MovementServiceStatusChanger(statusRepositoryMock);

    private Status status = null;

    @Before
    public void Setup(){
        status = Status.builder().build();
        when(statusRepositoryMock.CurrentStatus()).thenReturn(status);
        doAnswer(i -> {
            status = i.getArgument(0, Status.class);
            return null;
        }).when(statusRepositoryMock).Save(any());
    }

    @Test
    public void StatusShouldBeEmpty(){
        assertEquals(Status.builder().build(), status);
    }
}
