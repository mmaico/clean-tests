package mmaico.smoothtest.sellers.infrastructure.dao;


import dev.failsafe.Failsafe;
import dev.failsafe.RetryPolicy;
import mmaico.smoothtest.infrastructure.http.RestTemplateResponseErrorHandler;
import mmaico.smoothtest.sellers.infrastructure.dao.dto.SellerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static java.time.Duration.ofMillis;
import static org.springframework.http.HttpStatus.OK;

@Component
public class SellerDAO {

    protected static final Logger LOGGER = LoggerFactory.getLogger(SellerDAO.class);
    private static final String TEMPLATE = "result={} elapsedTime={} method={} uri={}";
    private static final String URI = "/sellers/{id}";

    private static final RetryPolicy<Optional<SellerDTO>> retryPolicy = RetryPolicy.<Optional<SellerDTO>>builder()
            .withMaxRetries(5)
            .withDelay(ofMillis(1000))
            .build();


    private RestTemplate restTemplate;

    @Value("${seller.service}")
    private String host;


    public SellerDAO(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    }

    public Optional<SellerDTO> getSeller(String sellerId) {
        String uri = URI.replace("{id}", sellerId);
        return Failsafe.with(retryPolicy).get(() -> {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            ResponseEntity<SellerDTO> response = restTemplate.getForEntity(host + uri, SellerDTO.class);

            stopWatch.stop();
            LOGGER.info(TEMPLATE, response.getStatusCode(), stopWatch.getTotalTimeMillis(),  "GET", uri);
            if (response.getStatusCode() != OK) {
                throw new RuntimeException("Retry again");
            }
            return response.getStatusCode() == OK ? Optional.of(response.getBody()) : Optional.empty();
        });
    }
}
