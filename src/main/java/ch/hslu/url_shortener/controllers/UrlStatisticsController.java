package ch.hslu.url_shortener.controllers;

import ch.hslu.url_shortener.dtos.StatisticsDto;
import ch.hslu.url_shortener.entities.UrlStatistics;
import ch.hslu.url_shortener.services.UrlStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

@RestController
public class UrlStatisticsController {
    private final UrlStatisticsService urlStatisticsService;

    public UrlStatisticsController(UrlStatisticsService urlStatisticsService) {
        this.urlStatisticsService = urlStatisticsService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsDto> getStatistics() {
        List<UrlStatistics> urlStatistics = urlStatisticsService.getAllUrlStatistics();
        StatisticsDto statisticsDto = new StatisticsDto();
        OptionalDouble averageForwardDurationInMillis = urlStatistics.stream().mapToLong(u -> u.getAverageForwardDurationInMillis()).average();
        if (averageForwardDurationInMillis.isPresent()) {
            statisticsDto.setAverageForwardDurationInMillis((long)averageForwardDurationInMillis.getAsDouble());
        }

        long totalNumberOfCalls = urlStatistics.stream().mapToLong(u -> u.getTotalNumberOfCalls()).sum();
        statisticsDto.setTotalNumberOfCalls(totalNumberOfCalls);

        Optional<OffsetDateTime> timeOfLastCall = urlStatistics.stream().map(u -> u.getTimeOfLastCall()).max(OffsetDateTime::compareTo);
        if (timeOfLastCall.isPresent()) {
            statisticsDto.setTimeOfLastCall(timeOfLastCall.get());
        }

        return ResponseEntity.ok(statisticsDto);
    }

    @GetMapping("/shorturls/{id}/statistics")
    public ResponseEntity<UrlStatistics> getUrlStatisticsByUrlId(@PathVariable(name = "id") UUID urlId) {
        UrlStatistics urlStatistics = urlStatisticsService.getUrlStatisticsByUrlId(urlId);
        return ResponseEntity.ok(urlStatistics);
    }
}
