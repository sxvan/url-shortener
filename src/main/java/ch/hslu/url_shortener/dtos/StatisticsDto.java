package ch.hslu.url_shortener.dtos;

import java.time.OffsetDateTime;

public class StatisticsDto {
    private long totalNumberOfCalls;
    private long averageForwardDurationInMillis;
    private OffsetDateTime timeOfLastCall;

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
}
