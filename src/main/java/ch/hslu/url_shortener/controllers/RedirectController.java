package ch.hslu.url_shortener.controllers;

import ch.hslu.url_shortener.configuration.AppConfig;
import ch.hslu.url_shortener.constants.Routes;
import ch.hslu.url_shortener.entities.Url;
import ch.hslu.url_shortener.services.UrlService;
import ch.hslu.url_shortener.services.UrlStatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.OffsetDateTime;
import java.util.Optional;

@Controller
@RequestMapping(Routes.REDIRECT)
public class RedirectController {
    private final UrlService urlService;
    private final UrlStatisticsService urlStatisticsService;
    private final AppConfig appConfig;

    public RedirectController(UrlService urlService, UrlStatisticsService urlStatisticsService, AppConfig appConfig) {
        this.urlService = urlService;
        this.urlStatisticsService = urlStatisticsService;
        this.appConfig = appConfig;
    }

    @GetMapping("/{short}")
    public String redirect(@PathVariable("short") String shortUrl, Model model) {
        long startingTime = System.currentTimeMillis();
        Optional<Url> optionalUrl = urlService.getUrlByShortUrl(shortUrl);
        if (optionalUrl.isEmpty()) {
            return "404";
        }

        Url url = optionalUrl.get();
        model.addAttribute("url", url.getLongUrl());
        model.addAttribute("timeout", appConfig.getRedirectDelayInMillis());


        long endTime = System.currentTimeMillis();
        long redirectTimeInMillis = endTime - startingTime + appConfig.getRedirectDelayInMillis();
        urlStatisticsService.addUrlCall(url.getId(), redirectTimeInMillis, OffsetDateTime.now());
        return "intermediate";
    }
}
