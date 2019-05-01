package com.tddapps.rt.api.controllers;

import com.tddapps.rt.ioc.IocContainer;
import com.tddapps.rt.model.Position;
import com.tddapps.rt.model.Status;
import com.tddapps.rt.model.StatusRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.tddapps.rt.test.IocContainerAutoMappingHelper.SetupAutoMapping;
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

    private void SeedStatus(Status seededStatus) {
        var statusRepositoryMock = mock(StatusRepository.class);
        when(statusRepositoryMock.CurrentStatus()).thenReturn(seededStatus);
        when(containerMock.Resolve(StatusRepository.class))
                .thenReturn(statusRepositoryMock);
    }

    @Before
    public void Setup(){
        SetupAutoMapping(containerMock);
    }

    @Test
    public void ReturnsTheMappedStatus() throws Exception {
        var seededStatus = Status.builder()
                .currentPosition(new Position(34.6333333, 12.253))
                .commandedPosition(new Position(134.6333333, 112.2544))
                .isMoving(true)
                .isCalibrating(true)
                .isCalibrated(true)
                .isHardwareInitialized(true)
                .isHardwareCrash(true)
                .build();
        SeedStatus(seededStatus);

        var expected = "{"
                + "\"isCalibrating\": true, "
                + "\"isCalibrated\": true, "
                + "\"isHardwareInitialized\": true,"
                + "\"isHardwareCrash\": true,"
                + "\"isMoving\": true, "
                + "\"theta\": 34.63, "
                + "\"phi\": 12.25, "
                + "\"commandedTheta\": 134.63, "
                + "\"commandedPhi\": 112.25"
                + "}";

        web.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }
}
