package ch.hslu.url_shortener.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UrlDto {
    @JsonProperty("short")
    private String shortUrl;

    @JsonProperty("url")
    private String longUrl;

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
}
