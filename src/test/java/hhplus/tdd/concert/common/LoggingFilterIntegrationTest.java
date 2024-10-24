package hhplus.tdd.concert.common;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import hhplus.tdd.concert.app.infrastructure.DatabaseCleaner;
import hhplus.tdd.concert.common.config.LoggingFilter;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LoggingFilterIntegrationTest {

    @Autowired
    private LoggingFilter loggingFilter;

    @Autowired
    private MockMvc mockMvc;

    private ListAppender<ILoggingEvent> listAppender;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    public void setUp() {
        databaseCleaner.clear();
    }

    @BeforeEach
    void setup() {
        Logger logger = LoggerFactory.getLogger(loggingFilter.getClass());
        listAppender = new ListAppender<>();
        listAppender.start();
        ((ch.qos.logback.classic.Logger) logger).addAppender(listAppender);
    }

    @Test
    public void 로깅_request_response_확인() throws Exception {
        String url = "/api/v1/user/1/queue/token";

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.waitingToken").value(notNullValue()))
                .andReturn();

        boolean requestLogFound = listAppender.list.stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Request : POST uri=[" + url + "]"));
        assertThat(requestLogFound).isTrue();

        boolean responseLogFound = listAppender.list.stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Payload : {\"waitingToken\":"));
        assertThat(responseLogFound).isTrue();
    }

}