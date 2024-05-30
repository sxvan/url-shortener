package ch.hslu.url_shortener.controllers;

import ch.hslu.url_shortener.dtos.StatisticsDto;
import ch.hslu.url_shortener.entities.Url;
import ch.hslu.url_shortener.entities.UrlStatistics;
import ch.hslu.url_shortener.services.UrlService;
import ch.hslu.url_shortener.services.UrlStatisticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UrlStatisticsController.class)
@AutoConfigureMockMvc(addFilters = false)
class UrlStatisticsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlStatisticsService urlStatisticsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    void test_getAllUrlStatistics_returnsAllUrlStatistics() throws Exception {
        List<UrlStatistics> urlStatistics = Arrays.asList(
                new UrlStatistics(UUID.randomUUID(), 1, 1, OffsetDateTime.now()),
                new UrlStatistics(UUID.randomUUID(), 2, 2, OffsetDateTime.now()),
                new UrlStatistics(UUID.randomUUID(), 3, 3, OffsetDateTime.now())
        );

        StatisticsDto statisticsDto = new StatisticsDto();
        statisticsDto.setTotalNumberOfCalls(6);
        statisticsDto.setAverageForwardDurationInMillis(2);
        statisticsDto.setTimeOfLastCall(urlStatistics.stream().map(u -> u.getTimeOfLastCall()).max(OffsetDateTime::compareTo).get());

        when(urlStatisticsService.getAllUrlStatistics()).thenReturn(urlStatistics);

        String expectedJson = objectMapper.writeValueAsString(statisticsDto);

        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    void test_getUrlStatisticsByUrlId_returnsUrlStatistics() throws Exception {
        UrlStatistics urlStatistics = new UrlStatistics(UUID.randomUUID(), 1, 1, OffsetDateTime.now());
        Url url = new Url(UUID.randomUUID(), "1", "1");
        urlStatistics.setUrl(url);

        when(urlStatisticsService.getUrlStatisticsByUrlId(url.getId())).thenReturn(urlStatistics);

        String expectedJson = objectMapper.writeValueAsString(urlStatistics);

        mockMvc.perform(get("/shorturls/" + url.getId() + "/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));
    }
}