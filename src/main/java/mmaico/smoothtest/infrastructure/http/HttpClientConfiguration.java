package mmaico.smoothtest.infrastructure.http;

import com.squareup.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfiguration {

    @Bean
    public OkHttpClient httpClient() {
        OkHttpClient client = new OkHttpClient();
        return client;
    }

}
