package org.mosesidowu.boxdelivery.config;

import org.mosesidowu.geolocation_core.service.googleService.GoogleService;
import org.mosesidowu.geolocation_core.service.googleService.GoogleServiceInterface;
import org.mosesidowu.geolocation_core.service.rateLimiterService.RateLimiterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GeoConfig {

    @Bean
    public GoogleService googleService() {
        return new GoogleService(new RestTemplate(), new RateLimiterService()); // import from the dependency
    }
}
