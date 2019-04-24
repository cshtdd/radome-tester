package com.tddapps.rt.api.controllers;

import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.model.SettingsReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StatusController.class)
public class StatusControllerTest {
    @Autowired
    private MockMvc web;

    @MockBean
    private IocContainer containerMock;

    @Test
    public void ReturnsHelloWorldAndTheEnvironment() throws Exception {
        var settingsReaderMock = mock(SettingsReader.class);
        when(settingsReaderMock.Read("app_env", "dev"))
                .thenReturn("lab");

        when(containerMock.Resolve(SettingsReader.class))
                .thenReturn(settingsReaderMock);

        web.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello World!")))
                .andExpect(content().string(containsString("env: lab")));
    }
}
