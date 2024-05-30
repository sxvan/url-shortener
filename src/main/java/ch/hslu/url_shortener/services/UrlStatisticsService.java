package ch.hslu.url_shortener.services;

import ch.hslu.url_shortener.entities.UrlStatistics;
import ch.hslu.url_shortener.repositories.UrlStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlStatisticsService {
    private final UrlStatisticsRepository urlStatisticsRepository;

    @Autowired
    public UrlStatisticsService(UrlStatisticsRepository urlStatisticsRepository) {
        this.urlStatisticsRepository = urlStatisticsRepository;
    }

    public List<UrlStatistics> getAllUrlStatistics() {
        return urlStatisticsRepository.findAll();
    }

    public UrlStatistics getUrlStatisticsByUrlId(UUID urlId) {
        return urlStatisticsRepository.findByUrlId(urlId)
                .orElseThrow(() -> new IllegalArgumentException("URL statistics not found with url ID: " + urlId));
    }

    public UrlStatistics addUrlCall(UUID urlId, long forwardDurationInMillis, OffsetDateTime timeOfLastCall) {
        UrlStatistics urlStatistics = getUrlStatisticsByUrlId(urlId);
        urlStatistics.setAverageForwardDurationInMillis((urlStatistics.getAverageForwardDurationInMillis() * urlStatistics.getTotalNumberOfCalls() + forwardDurationInMillis) / (urlStatistics.getTotalNumberOfCalls() + 1));
        urlStatistics.setTotalNumberOfCalls(urlStatistics.getTotalNumberOfCalls() + 1);
        urlStatistics.setTimeOfLastCall(timeOfLastCall);

        return urlStatisticsRepository.save(urlStatistics);
    }
}
