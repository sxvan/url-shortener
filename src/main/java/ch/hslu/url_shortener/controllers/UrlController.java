package ch.hslu.url_shortener.controllers;

import ch.hslu.url_shortener.constants.Routes;
import ch.hslu.url_shortener.dtos.UrlDto;
import ch.hslu.url_shortener.entities.Url;
import ch.hslu.url_shortener.models.AddOrUpdateResult;
import ch.hslu.url_shortener.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(Routes.URL)
public class UrlController {
    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping()
    public ResponseEntity<List<Url>> getAllUrls() {
        List<Url> urls = urlService.getAllUrls();
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Url> getUrlById(@PathVariable UUID id) {
        Optional<Url> optionalUrl = urlService.getUrlById(id);
        return optionalUrl
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Url> addOrUpdateUrl(@PathVariable UUID id, @RequestBody UrlDto urlDto) {
        Optional<Url> optionalUrl = urlService.getUrlByShortUrl(urlDto.getShortUrl());
        if (optionalUrl.isPresent() && !id.equals(optionalUrl.get().getId())) {
            return ResponseEntity.badRequest().build();
        }

        AddOrUpdateResult<Url> addOrUpdateUrlResult = urlService.addOrUpdateUrl(id, urlDto.getShortUrl(), urlDto.getLongUrl());
        Url url = addOrUpdateUrlResult.getEntity();
        if (addOrUpdateUrlResult.isAdded()) {
            return ResponseEntity.created(URI.create(Routes.URL + "/" + url.getId()))
                    .body(url);
        }

        return ResponseEntity.ok(url);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Url> deleteUrl(@PathVariable UUID id) {
        urlService.deleteUrl(id);
        return ResponseEntity.ok().build();
    }
}
