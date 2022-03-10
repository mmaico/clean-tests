package mmaico.lazytests.infrastructure.http;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateWrapper {
    private RestTemplate restTemplate;

    public RestTemplateWrapper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public <T> Response<T> getForEntity(String url, Class<T> clazz) {
        try {
            ResponseEntity<T> entity = restTemplate.getForEntity(url, clazz);
        } catch(Exception e) {
            return null;
        }

        return null;
    }

    public class Response<T> {
        private HttpStatus status;
        private T body;
        private long elapsedTime;
        private HttpMethod method;

        public HttpStatus getStatus() {
            return status;
        }

        public void setStatus(HttpStatus status) {
            this.status = status;
        }

        public T getBody() {
            return body;
        }

        public void setBody(T body) {
            this.body = body;
        }

        public long getElapsedTime() {
            return elapsedTime;
        }

        public void setElapsedTime(long elapsedTime) {
            this.elapsedTime = elapsedTime;
        }

        public HttpMethod getMethod() {
            return method;
        }

        public void setMethod(HttpMethod method) {
            this.method = method;
        }
    }

}
