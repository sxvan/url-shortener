package ch.hslu.url_shortener.controllers;

import ch.hslu.url_shortener.dtos.UrlDto;
import ch.hslu.url_shortener.entities.Url;
import ch.hslu.url_shortener.models.AddOrUpdateResult;
import ch.hslu.url_shortener.services.UrlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UrlController.class)
@AutoConfigureMockMvc(addFilters = false)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlService urlService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test_getAllUrls_withoutCredentials_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/shorturls"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    void test_getAllUrls_returnsAllUrls() throws Exception {
        List<Url> urls = Arrays.asList(
                new Url(UUID.randomUUID(), "1", "1"),
                new Url(UUID.randomUUID(), "2", "2"),
                new Url(UUID.randomUUID(), "3", "3")
        );

        when(urlService.getAllUrls()).thenReturn(urls);

        String expectedJson = objectMapper.writeValueAsString(urls);

        mockMvc.perform(get("/shorturls"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    void test_getUrlById_returnsUrl() throws Exception {
        Url url = new Url(UUID.randomUUID(), "1", "1");
        when(urlService.getUrlById(url.getId())).thenReturn(Optional.of(url));

        String expectedJson = objectMapper.writeValueAsString(url);

        mockMvc.perform(get("/shorturls/" + url.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    void test_getUrlById_invalidId_returnsNotFound() throws Exception {
        Url url = new Url(UUID.randomUUID(), "1", "1");
        when(urlService.getUrlById(url.getId())).thenReturn(Optional.of(url));

        mockMvc.perform(get("/shorturls/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    void test_addOrUpdateUrl_addUrl_returnsUrl() throws Exception {
        Url url = new Url(UUID.randomUUID(), "1", "1");
        AddOrUpdateResult<Url> addOrUpdateResult = new AddOrUpdateResult<>(url, true);
        when(urlService.addOrUpdateUrl(url.getId(), url.getShortUrl(), url.getLongUrl())).thenReturn(addOrUpdateResult);

        UrlDto urlDto = new UrlDto();
        urlDto.setShortUrl(url.getShortUrl());
        urlDto.setLongUrl(url.getLongUrl());

        String body = objectMapper.writeValueAsString(urlDto);
        String expectedJson = objectMapper.writeValueAsString(url);

        mockMvc.perform(put("/shorturls/" + url.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedJson));
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    void test_addOrUpdateUrl_updateUrl_returnsUrl() throws Exception {
        Url url = new Url(UUID.randomUUID(), "1", "1");
        AddOrUpdateResult<Url> addOrUpdateResult = new AddOrUpdateResult<>(url, false);
        when(urlService.addOrUpdateUrl(url.getId(), url.getShortUrl(), url.getLongUrl())).thenReturn(addOrUpdateResult);

        UrlDto urlDto = new UrlDto();
        urlDto.setShortUrl(url.getShortUrl());
        urlDto.setLongUrl(url.getLongUrl());

        String body = objectMapper.writeValueAsString(urlDto);
        String expectedJson = objectMapper.writeValueAsString(url);

        mockMvc.perform(put("/shorturls/" + url.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    void test_deleteUrl_returnOk() throws Exception {
        mockMvc.perform(delete("/shorturls/" + UUID.randomUUID()))
                .andExpect(status().isOk());
    }
}