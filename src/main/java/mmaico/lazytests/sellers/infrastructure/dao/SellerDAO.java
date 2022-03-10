package mmaico.lazytests.sellers.infrastructure.dao;


import dev.failsafe.Failsafe;
import dev.failsafe.RetryPolicy;
import mmaico.lazytests.sellers.infrastructure.dao.dto.SellerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.time.Duration.ofMillis;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Component
public class SellerDAO {

    protected static final Logger LOGGER = LoggerFactory.getLogger(SellerDAO.class);
    private static final String TEMPLATE = "result={} method={} uri={}";

    private static final String URI_GET = "/api/salesman/{id}";
    private static final String URI_ROOT = "/api/salesman";

    private static final RetryPolicy<Optional<SellerDTO>> retryPolicy = RetryPolicy.<Optional<SellerDTO>>builder()
            .withMaxRetries(5)
            .withDelay(ofMillis(1000))
            .build();

    private static final RetryPolicy<List<SellerDTO>> retryPolicyList = RetryPolicy.<List<SellerDTO>>builder()
            .withMaxRetries(3)
            .withDelay(ofMillis(1000))
            .build();


    private RestTemplate restTemplate;

    @Value("${seller.service}")
    private String host;


    public SellerDAO(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<SellerDTO> getSeller(String sellerId) {
        String uri = URI_GET.replace("{id}", sellerId);
        return Failsafe.with(retryPolicy).get(() -> {
            ResponseEntity<SellerDTO> response = restTemplate.getForEntity(host + uri, SellerDTO.class);

            if (response.getStatusCode() != OK) {
                throw new RuntimeException("Retry again");
            }
            LOGGER.info(TEMPLATE, response.getStatusCode(),  "GET", host + uri);
            return response.getStatusCode() == OK ? Optional.of(response.getBody()) : Optional.empty();
        });
    }

    public List<SellerDTO> getAll() {
        return Failsafe.with(retryPolicyList).get(() -> {
            ResponseEntity<SellerDTO[]> response = restTemplate.getForEntity(host + URI_ROOT, SellerDTO[].class);
            if (response.getStatusCode() != OK) throw new RuntimeException("Retry again");

            return response.getStatusCode() == OK ? Arrays.asList(response.getBody()) : new ArrayList<>();
        });
    }

    public Optional<SellerDTO> createSeller(SellerDTO dto) {
        return Failsafe.with(retryPolicy).get(() -> {
            HttpEntity<SellerDTO> request = new HttpEntity<>(dto);
            ResponseEntity<SellerDTO> response = restTemplate.postForEntity(host + URI_ROOT, request, SellerDTO.class);

            if (response.getStatusCode() != CREATED) {
                throw new RuntimeException("Retry again");
            }
            return response.getStatusCode() == CREATED ? Optional.of(response.getBody()) : Optional.empty();
        });

    }
}
