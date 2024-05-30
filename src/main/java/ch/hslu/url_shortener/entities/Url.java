package ch.hslu.url_shortener.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.util.StringTokenizer;
import java.util.UUID;

@Entity
public class Url {
    @Id
    private UUID id;

    @Column(unique = true)
    private String shortUrl;
    private String longUrl;

    @OneToOne(mappedBy = "url", cascade = CascadeType.ALL)
    @JsonIgnore
    private UrlStatistics urlStatistics;

    public Url(UUID id, String shortUrl, String longUrl) {
        this.id = id;
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
    }

    public Url() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public UrlStatistics getUrlStatistics() {
        return urlStatistics;
    }

    public void setUrlStatistics(UrlStatistics urlStatistics) {
        this.urlStatistics = urlStatistics;
    }
}
