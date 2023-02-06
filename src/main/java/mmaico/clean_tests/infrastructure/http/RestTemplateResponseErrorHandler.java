package mmaico.clean_tests.infrastructure.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);
    private static final String TEMPLATE = "result={} method={} uri={}";

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        System.out.println("Error");
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        LOGGER.info(TEMPLATE, response.getStatusCode(),  method.name(), url.toString());
    }
}
