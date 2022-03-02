package mmaico.smoothtest.sellers.infrastructure.dao;


import dev.failsafe.Failsafe;
import dev.failsafe.RetryPolicy;
import mmaico.smoothtest.sellers.infrastructure.dao.dto.LevelDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static java.time.Duration.ofMillis;
import static org.springframework.http.HttpStatus.OK;

@Component
public class LevelDAO {

    private static final String URI = "/sellers/{id}/levels";

    private static final RetryPolicy<Integer> retryPolicy = RetryPolicy.<Integer>builder()
            .withMaxRetries(5)
            .withDelay(ofMillis(1000))
            .build();

    private RestTemplate restTemplate;

    @Value("${seller.service}")
    private String host;


    public LevelDAO(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public int getLevelBy(String sellerId) {
        String uri = URI.replace("{id}", sellerId);
        return Failsafe.with(retryPolicy).get(() -> {
            ResponseEntity<LevelDTO> response = restTemplate.getForEntity(host + uri, LevelDTO.class);
            if (response.getStatusCode() != OK) {
                throw new RuntimeException("Retry again");
            }
            return response.getStatusCode() == OK ? response.getBody().getValue() : 0;
        });
    }
}
