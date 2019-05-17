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

import static com.tddapps.rt.mapping.internal.IocContainerAutoMappingHelper.setupAutoMapping;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(StatusController.class)
public class StatusControllerTest {
    @Autowired
    private MockMvc web;

    @MockBean
    private IocContainer containerMock;

    private void SeedStatus(Status seededStatus) {
        var statusRepositoryMock = mock(StatusRepository.class);
        when(statusRepositoryMock.read()).thenReturn(seededStatus);
        when(containerMock.resolve(StatusRepository.class))
                .thenReturn(statusRepositoryMock);
    }

    @Before
    public void setup(){
        setupAutoMapping(containerMock);
    }

    @Test
    public void returnsTheMappedStatus() throws Exception {
        var seededStatus = Status.builder()
                .currentPosition(new Position(34.6333333, 12.253))
                .commandedPosition(new Position(134.6333333, 112.2544))
                .isPanning(true)
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
                + "\"isPanning\": true, "
                + "\"isMoving\": true, "
                + "\"theta\": 34.63, "
                + "\"phi\": 12.25, "
                + "\"commandedTheta\": 134.63, "
                + "\"commandedPhi\": 112.25"
                + "}";

        web.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected))
                .andExpect(jsonPath("$.calibrating").doesNotExist())
                .andExpect(jsonPath("$.calibrated").doesNotExist())
                .andExpect(jsonPath("$.hardwareInitialized").doesNotExist())
                .andExpect(jsonPath("$.hardwareCrash").doesNotExist())
                .andExpect(jsonPath("$.panning").doesNotExist())
                .andExpect(jsonPath("$.moving").doesNotExist());
    }
}
