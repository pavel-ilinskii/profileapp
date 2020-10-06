package com.ilinskii.profileapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilinskii.profileapp.api.errorlog.ErrorLogResponse;
import com.ilinskii.profileapp.repository.ErrorLogRepository;
import com.ilinskii.profileapp.service.ErrorLogService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.ilinskii.profileapp.config.SecurityConfig.SEC_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ErrorLogTest extends AbstractTest {

    @Value("${api-key}")
    private String apiKey;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ErrorLogService errorLogService;

    @Autowired
    private ErrorLogRepository errorLogRepository;

    @BeforeEach
    public void beforeEach() {
        errorLogRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    public void get_last_error_log_not_found() {
        mockMvc.perform(
                get("/error/last")
                        .header(SEC_HEADER, apiKey))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void get_last_error_log() {
        errorLogService.logError("msg1");
        errorLogService.logError("msg2");

        var errorLogResponse = mockMvc.perform(
                get("/error/last")
                        .header(SEC_HEADER, apiKey))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        var errorLog = objectMapper.readValue(errorLogResponse.getContentAsString(), ErrorLogResponse.class);

        assertThat(errorLog.getMsg()).isEqualTo("msg2");
        assertThat(errorLog.getCreated()).isNotNull();
    }
}
