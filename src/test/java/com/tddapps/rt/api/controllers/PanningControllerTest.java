package com.tddapps.rt.api.controllers;

import com.tddapps.rt.InvalidOperationException;
import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.model.MovementService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PanningController.class)
public class PanningControllerTest {
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

    private ResultActions Post() throws Exception {
        var request = post("/api/panning")
                .contentType(MediaType.APPLICATION_JSON);
        return web.perform(request);
    }

    @Test
    public void Returns400WhenCannotPan() throws Exception {
        when(movementServiceMock.CanPan()).thenReturn(false);

        Post()
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Cannot Pan"));
    }

    @Test
    public void Returns400WhenPanningIsInvalid() throws Exception {
        when(movementServiceMock.CanPan()).thenReturn(true);
        doThrow(new InvalidOperationException()).when(movementServiceMock).Pan();

        Post()
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Panning Error"));
    }

    @Test
    public void Returns500WhenMovementFails() throws Exception {
        when(movementServiceMock.CanPan()).thenReturn(true);
        doAnswer(i -> {
            throw new Exception();
        }).when(movementServiceMock).Pan();

        Post()
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Unexpected Error"));
    }

    @Test
    public void ReturnsOkWhenPanningSucceeds() throws Exception {
        when(movementServiceMock.CanPan()).thenReturn(true);

        Post()
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(movementServiceMock).Pan();
    }
}
