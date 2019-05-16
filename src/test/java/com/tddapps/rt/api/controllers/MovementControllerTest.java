package com.tddapps.rt.api.controllers;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.model.MovementService;
import com.tddapps.rt.model.Position;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.tddapps.rt.mapping.internal.IocContainerAutoMappingHelper.SetupAutoMapping;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(MovementController.class)
public class MovementControllerTest {
    @Autowired
    private MockMvc web;

    @MockBean
    private IocContainer containerMock;

    private final MovementService movementServiceMock = mock(MovementService.class);

    @Before
    public void Setup(){
        SetupAutoMapping(containerMock);
        when(containerMock.Resolve(MovementService.class)).thenReturn(movementServiceMock);
    }

    private ResultActions StartMovement(String jsonBody) throws Exception {
        var request = post("/api/movement/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody);
        return web.perform(request);
    }

    private ResultActions StopMovement() throws Exception {
        var request = post("/api/movement/stop")
                .contentType(MediaType.APPLICATION_JSON);
        return web.perform(request);
    }

    private ResultActions StartPanning() throws Exception {
        var request = post("/api/movement/pan")
                .contentType(MediaType.APPLICATION_JSON);
        return web.perform(request);
    }

    @Test
    public void Returns400WhenCannotMove() throws Exception {
        when(movementServiceMock.CanMove(new Position(190, 85)))
                .thenReturn(false);

        StartMovement("{\"theta\": 190, \"phi\": 85}")
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Cannot Move"));
    }

    @Test
    public void Returns400WhenMovementIsInvalid() throws Exception {
        when(movementServiceMock.CanMove(any()))
                .thenReturn(true);
        doThrow(new InvalidOperationException())
                .when(movementServiceMock).Move(any());

        StartMovement("{\"theta\": 190, \"phi\": 86}")
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Movement Error"));
    }

    @Test
    public void Returns500WhenMovementFails() throws Exception {
        when(movementServiceMock.CanMove(any()))
                .thenReturn(true);
        doAnswer(i -> {
            throw new Exception();
        }).when(movementServiceMock).Move(any());

        StartMovement("{\"theta\": 190, \"phi\": 86}")
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Unexpected Error"));
    }

    @Test
    public void ReturnsOkWhenMovementSucceeds() throws Exception {
        when(movementServiceMock.CanMove(any()))
                .thenReturn(true);

        StartMovement("{\"theta\": 191.333333, \"phi\": 84.66666}")
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(movementServiceMock).Move(new Position(191.33, 84.67));
    }

    @Test
    public void Returns500WhenStopFails() throws Exception {
        doAnswer(i -> {
            throw new Exception();
        }).when(movementServiceMock).Stop();

        StopMovement()
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Unexpected Error"));
    }

    @Test
    public void ReturnsOkWhenStopSucceeds() throws Exception {
        StopMovement()
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(movementServiceMock).Stop();
    }

    @Test
    public void Returns400WhenCannotPan() throws Exception {
        when(movementServiceMock.CanPan()).thenReturn(false);

        StartPanning()
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Cannot Pan"));
    }

    @Test
    public void Returns400WhenPanningIsInvalid() throws Exception {
        when(movementServiceMock.CanPan()).thenReturn(true);
        doThrow(new InvalidOperationException()).when(movementServiceMock).Pan();

        StartPanning()
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Panning Error"));
    }

    @Test
    public void Returns500WhenPanningFails() throws Exception {
        when(movementServiceMock.CanPan()).thenReturn(true);
        doAnswer(i -> {
            throw new Exception();
        }).when(movementServiceMock).Pan();

        StartPanning()
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Unexpected Error"));
    }

    @Test
    public void ReturnsOkWhenPanningSucceeds() throws Exception {
        when(movementServiceMock.CanPan()).thenReturn(true);

        StartPanning()
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(movementServiceMock).Pan();
    }
}
