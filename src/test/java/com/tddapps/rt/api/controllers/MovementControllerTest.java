package com.tddapps.rt.api.controllers;

import com.tddapps.rt.ioc.IocContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MovementController.class)
public class MovementControllerTest {
    @Autowired
    private MockMvc web;

    @MockBean
    private IocContainer containerMock;

    @Test
    public void ReturnsOkByDefault() throws Exception {
        var requestBody = "{\"thetaDegrees\": 190, \"phiDegrees\": 85}";

        var request = post("/api/movement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        web.perform(request).andExpect(status().isOk());
    }
}
