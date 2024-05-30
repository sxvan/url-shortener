package ch.hslu.url_shortener.controllers;

import ch.hslu.url_shortener.dtos.UrlDto;
import ch.hslu.url_shortener.entities.Url;
import ch.hslu.url_shortener.services.UrlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc()
class UrlControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    @AfterEach
    void clearDatabase() {
        jdbcTemplate.execute("DELETE FROM url_statistics");
        jdbcTemplate.execute("DELETE FROM url");
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    void test_addOrUpdateUrl_returnsAllUrls() throws Exception {
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
    }
}
