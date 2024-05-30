package ch.hslu.url_shortener.entities;

import ch.hslu.url_shortener.services.UrlStatisticsService;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
public class UrlStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private long totalNumberOfCalls;
    private long averageForwardDurationInMillis;
    private OffsetDateTime timeOfLastCall;

    @OneToOne
    @JoinColumn(name = "url_id")
    private Url url;

    public UrlStatistics() {

    }

    public UrlStatistics(UUID id, long totalNumberOfCalls, long averageForwardDurationInMillis, OffsetDateTime timeOfLastCall) {
        this.id = id;
        this.totalNumberOfCalls = totalNumberOfCalls;
        this.averageForwardDurationInMillis = averageForwardDurationInMillis;
        this.timeOfLastCall = timeOfLastCall;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getTotalNumberOfCalls() {
        return totalNumberOfCalls;
    }

    public void setTotalNumberOfCalls(long totalNumberOfCalls) {
        this.totalNumberOfCalls = totalNumberOfCalls;
    }

    public long getAverageForwardDurationInMillis() {
        return averageForwardDurationInMillis;
    }

    public void setAverageForwardDurationInMillis(long averageForwardDurationInMillis) {
        this.averageForwardDurationInMillis = averageForwardDurationInMillis;
    }

    public OffsetDateTime getTimeOfLastCall() {
        return timeOfLastCall;
    }

    public void setTimeOfLastCall(OffsetDateTime timeOfLastCall) {
        this.timeOfLastCall = timeOfLastCall;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }
}
