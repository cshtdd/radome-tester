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

import static com.tddapps.rt.mapping.internal.IocContainerAutoMappingHelper.setupAutoMapping;
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
    public void setup(){
        setupAutoMapping(containerMock);
        when(containerMock.resolve(MovementService.class)).thenReturn(movementServiceMock);
    }

    private ResultActions startMovement(String jsonBody) throws Exception {
        var request = post("/api/movement/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody);
        return web.perform(request);
    }

    private ResultActions stopMovement() throws Exception {
        var request = post("/api/movement/stop")
                .contentType(MediaType.APPLICATION_JSON);
        return web.perform(request);
    }

    private ResultActions startPanning() throws Exception {
        var request = post("/api/movement/pan")
                .contentType(MediaType.APPLICATION_JSON);
        return web.perform(request);
    }

    @Test
    public void returns400WhenCannotMove() throws Exception {
        when(movementServiceMock.canMove(new Position(190, 85)))
                .thenReturn(false);

        startMovement("{\"theta\": 190, \"phi\": 85}")
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Cannot Move"));
    }

    @Test
    public void returns400WhenMovementIsInvalid() throws Exception {
        when(movementServiceMock.canMove(any()))
                .thenReturn(true);
        doThrow(new InvalidOperationException())
                .when(movementServiceMock).move(any());

        startMovement("{\"theta\": 190, \"phi\": 86}")
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Movement Error"));
    }

    @Test
    public void returns500WhenMovementFails() throws Exception {
        when(movementServiceMock.canMove(any()))
                .thenReturn(true);
        doAnswer(i -> {
            throw new Exception();
        }).when(movementServiceMock).move(any());

        startMovement("{\"theta\": 190, \"phi\": 86}")
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Unexpected Error"));
    }

    @Test
    public void returnsOkWhenMovementSucceeds() throws Exception {
        when(movementServiceMock.canMove(any()))
                .thenReturn(true);

        startMovement("{\"theta\": 191.333333, \"phi\": 84.66666}")
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(movementServiceMock).move(new Position(191.33, 84.67));
    }

    @Test
    public void returns500WhenStopFails() throws Exception {
        doAnswer(i -> {
            throw new Exception();
        }).when(movementServiceMock).stop();

        stopMovement()
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Unexpected Error"));
    }

    @Test
    public void returnsOkWhenStopSucceeds() throws Exception {
        stopMovement()
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(movementServiceMock).stop();
    }

    @Test
    public void returns400WhenCannotPan() throws Exception {
        when(movementServiceMock.canPan()).thenReturn(false);

        startPanning()
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Cannot Pan"));
    }

    @Test
    public void returns400WhenPanningIsInvalid() throws Exception {
        when(movementServiceMock.canPan()).thenReturn(true);
        doThrow(new InvalidOperationException()).when(movementServiceMock).pan();

        startPanning()
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Panning Error"));
    }

    @Test
    public void returns500WhenPanningFails() throws Exception {
        when(movementServiceMock.canPan()).thenReturn(true);
        doAnswer(i -> {
            throw new Exception();
        }).when(movementServiceMock).pan();

        startPanning()
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Unexpected Error"));
    }

    @Test
    public void returnsOkWhenPanningSucceeds() throws Exception {
        when(movementServiceMock.canPan()).thenReturn(true);

        startPanning()
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(movementServiceMock).pan();
    }
}
