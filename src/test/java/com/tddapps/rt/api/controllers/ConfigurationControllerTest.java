package com.tddapps.rt.api.controllers;

import com.tddapps.rt.config.Configuration;
import com.tddapps.rt.config.ConfigurationReader;
import com.tddapps.rt.ioc.IocContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.tddapps.rt.mapping.internal.IocContainerAutoMappingHelper.setupAutoMapping;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ConfigurationController.class)
public class ConfigurationControllerTest {
    @Autowired
    private MockMvc web;

    @MockBean
    private IocContainer containerMock;

    private Configuration seededConfig = new Configuration();

    @Before
    public void setup(){
        setupAutoMapping(containerMock);

        var configurationReaderMock = mock(ConfigurationReader.class);
        when(configurationReaderMock.read())
                .thenReturn(seededConfig);

        when(containerMock.resolve(ConfigurationReader.class))
                .thenReturn(configurationReaderMock);
    }

    @Test
    public void returnsTheConfiguration() throws Exception {
        seededConfig.setThetaBcmPins(new int[]{20, 23, 25, 27});
        seededConfig.setPhiBcmPins(new int[]{120, 123, 125, 127});

        var expected = "{"
                + "\"simulation\": false,"
                + "\"thetaBcmPins\": [20, 23, 25, 27],"
                + "\"phiBcmPins\": [120, 123, 125, 127]"
                + "}";

        web.perform(get("/api/config"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }
}
