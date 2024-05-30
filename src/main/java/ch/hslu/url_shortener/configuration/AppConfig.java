package ch.hslu.url_shortener.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "url-shortener")
public class AppConfig {
    private int redirectDelayInMillis;


    public int getRedirectDelayInMillis() {
        return redirectDelayInMillis;
    }

    public void setRedirectDelayInMillis(int redirectDelayInMillis) {
        this.redirectDelayInMillis = redirectDelayInMillis;
    }
}
