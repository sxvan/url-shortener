package ch.hslu.url_shortener.services;

import ch.hslu.url_shortener.entities.Url;
import ch.hslu.url_shortener.entities.UrlStatistics;
import ch.hslu.url_shortener.models.AddOrUpdateResult;
import ch.hslu.url_shortener.repositories.UrlRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final UrlStatisticsService urlStatisticsService;

    @Autowired
    public UrlService(UrlRepository urlRepository, UrlStatisticsService urlStatisticsService) {
        this.urlRepository = urlRepository;
        this.urlStatisticsService = urlStatisticsService;
    }


    public List<Url> getAllUrls() {
        return urlRepository.findAll();
    }


    public Optional<Url> getUrlById(UUID id) {
        return urlRepository.findById(id);
    }

    public Optional<Url> getUrlByShortUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl);
    }

    public AddOrUpdateResult<Url> addOrUpdateUrl(UUID id, String shortUrl, String longUrl) {
        if (urlRepository.existsById(id)) {
            Url url = updateUrl(id, shortUrl, longUrl);
            return new AddOrUpdateResult<>(url, false);
        }

        Url url = addUrl(id, shortUrl, longUrl);
        return new AddOrUpdateResult<>(url, true);
    }

    public void deleteUrl(UUID id) {
        urlRepository.deleteById(id);
    }

    private Url updateUrl(UUID id, String shortUrl, String longUrl) {
        Url url = urlRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("URL not found with ID: " + id));
        url.setShortUrl(shortUrl);
        url.setLongUrl(longUrl);
        return urlRepository.save(url);
    }

    @Transactional
    private Url addUrl(UUID id, String shortUrl, String longUrl) {
        Url url = new Url(id, shortUrl, longUrl);

        UrlStatistics urlStatistics = new UrlStatistics();
        url.setUrlStatistics(urlStatistics);
        urlStatistics.setUrl(url);

        return urlRepository.save(url);
    }
}
