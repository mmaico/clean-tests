package mmaico.smoothtest.sellers.infrastructure.dao;


import dev.failsafe.Failsafe;
import dev.failsafe.RetryPolicy;
import mmaico.smoothtest.sellers.infrastructure.dao.dto.SellerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static java.time.Duration.ofMillis;
import static org.springframework.http.HttpStatus.OK;

@Component
public class SellerDAO {

    private static final String URI = "/sellers/{id}";

    private static final RetryPolicy<Optional<SellerDTO>> retryPolicy = RetryPolicy.<Optional<SellerDTO>>builder()
            .withMaxRetries(5)
            .withDelay(ofMillis(1000))
            .build();

    private RestTemplate restTemplate;


    public SellerDAO(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<SellerDTO> getSeller(String sellerId) {
        String uri = URI.replace("{id}", sellerId);
        return Failsafe.with(retryPolicy).get(() -> {
            ResponseEntity<SellerDTO> response = restTemplate.getForEntity(uri, SellerDTO.class);
            if (response.getStatusCode() != OK) {
                throw new RuntimeException("Retry again");
            }
            return response.getStatusCode() == OK ? Optional.of(response.getBody()) : Optional.empty();
        });
    }
}
