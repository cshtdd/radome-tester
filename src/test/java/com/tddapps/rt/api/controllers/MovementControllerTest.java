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
        when(containerMock.Resolve(MovementService.class)).thenReturn(movementServiceMock);
    }

    private ResultActions Post(String jsonBody) throws Exception {
        var request = post("/api/movement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody);
        return web.perform(request);
    }

    @Test
    public void Returns400WhenCannotMove() throws Exception {
        when(movementServiceMock.CanMove(new Position(190, 85)))
                .thenReturn(false);

        Post("{\"thetaDegrees\": 190, \"phiDegrees\": 85}")
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Cannot Move"));
    }

    @Test
    public void Returns400WhenMovementIsInvalid() throws Exception {
        when(movementServiceMock.CanMove(any()))
                .thenReturn(true);
        doThrow(new InvalidOperationException())
                .when(movementServiceMock).Move(any());

        Post("{\"thetaDegrees\": 190, \"phiDegrees\": 86}")
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

        Post("{\"thetaDegrees\": 190, \"phiDegrees\": 86}")
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Unexpected Error"));
    }

    @Test
    public void ReturnsOkWhenMovementSucceeds() throws Exception {
        when(movementServiceMock.CanMove(any()))
                .thenReturn(true);

        Post("{\"thetaDegrees\": 191, \"phiDegrees\": 84}")
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(movementServiceMock).Move(new Position(191, 84));
    }
}
