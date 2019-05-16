package com.tddapps.rt.api.controllers;

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
@WebMvcTest(MovementStopController.class)
public class MovementStopControllerTest {
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
        var request = post("/api/movement/stop")
                .contentType(MediaType.APPLICATION_JSON);
        return web.perform(request);
    }

    @Test
    public void Returns500WhenHaltingFails() throws Exception {
        doAnswer(i -> {
            throw new Exception();
        }).when(movementServiceMock).Stop();

        Post()
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Unexpected Error"));
    }

    @Test
    public void ReturnsOkWhenHaltingSucceeds() throws Exception {
        Post()
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(movementServiceMock).Stop();
    }
}
