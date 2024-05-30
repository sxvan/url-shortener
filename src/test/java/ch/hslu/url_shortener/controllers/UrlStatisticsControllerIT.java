package ch.hslu.url_shortener.controllers;

import ch.hslu.url_shortener.dtos.UrlDto;
import ch.hslu.url_shortener.entities.Url;
import ch.hslu.url_shortener.entities.UrlStatistics;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc()
class UrlStatisticsControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final MSSQLServerContainer<?> SQLSERVER_CONTAINER = new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2022-latest").acceptLicense();

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", SQLSERVER_CONTAINER::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", SQLSERVER_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", SQLSERVER_CONTAINER::getPassword);
    }

    @BeforeEach
    @AfterEach
    void clearDatabase() {
        jdbcTemplate.execute("DELETE FROM url_statistics");
        jdbcTemplate.execute("DELETE FROM url");
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    void test_urlControllerCRUDOperations() throws Exception {
        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.totalNumberOfCalls").value(0)));

        Url url = new Url(UUID.randomUUID(), "test", "test");
        UrlDto urlDto = new UrlDto();
        urlDto.setShortUrl(url.getShortUrl());
        urlDto.setLongUrl(url.getLongUrl());

        String body = objectMapper.writeValueAsString(urlDto);
        String expectedJson = objectMapper.writeValueAsString(url);

        mockMvc.perform(put("/shorturls/" + url.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedJson));

        mockMvc.perform(get("/shorturls/" + url.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));

        int callCount = 5;
        for (int i = 0; i < callCount; i++) {
            mockMvc.perform(get("/a/" + url.getShortUrl()))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/shorturls/" + url.getId() + "/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalNumberOfCalls").value(callCount));

        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.totalNumberOfCalls").value(callCount)));

        url = new Url(UUID.randomUUID(), "test2", "test2");
        urlDto = new UrlDto();
        urlDto.setShortUrl(url.getShortUrl());
        urlDto.setLongUrl(url.getLongUrl());

        body = objectMapper.writeValueAsString(urlDto);
        expectedJson = objectMapper.writeValueAsString(url);

        mockMvc.perform(put("/shorturls/" + url.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedJson));

        mockMvc.perform(get("/shorturls/" + url.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));

        for (int i = 0; i < callCount; i++) {
            mockMvc.perform(get("/a/" + url.getShortUrl()))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/shorturls/" + url.getId() + "/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalNumberOfCalls").value(callCount));

        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.totalNumberOfCalls").value(callCount * 2)));
    }
}
