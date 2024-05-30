package ch.hslu.url_shortener.controllers;

import ch.hslu.url_shortener.entities.UrlStatistics;
import ch.hslu.url_shortener.services.UrlStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class UrlStatisticsController {
    private final UrlStatisticsService urlStatisticsService;

    public UrlStatisticsController(UrlStatisticsService urlStatisticsService) {
        this.urlStatisticsService = urlStatisticsService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<UrlStatistics>> getAllUrlStatistics() {
        List<UrlStatistics> urlStatistics = urlStatisticsService.getAllUrlStatistics();
        return ResponseEntity.ok(urlStatistics);
    }

    @GetMapping("/shorturls/{id}/statistics")
    public ResponseEntity<UrlStatistics> getUrlStatisticsByUrlId(@PathVariable(name = "id") UUID urlId) {
        UrlStatistics urlStatistics = urlStatisticsService.getUrlStatisticsByUrlId(urlId);
        return ResponseEntity.ok(urlStatistics);
    }
}
