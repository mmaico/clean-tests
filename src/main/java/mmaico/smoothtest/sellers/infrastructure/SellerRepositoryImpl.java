package mmaico.smoothtest.sellers.infrastructure;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import dev.failsafe.RetryPolicy;
import dev.failsafe.okhttp.FailsafeCall;
import mmaico.smoothtest.sellers.domain.seller.Seller;
import mmaico.smoothtest.sellers.domain.seller.SellerRepository;
//import net.jodah.failsafe.RetryPolicy;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

import static java.time.Duration.ofMillis;

@Repository
public class SellerRepositoryImpl implements SellerRepository {

    private static final RetryPolicy retryPolicy = RetryPolicy.builder()
            .withMaxRetries(5)
            .withDelay(ofMillis(1000))
            .build();


    private OkHttpClient client;

    public SellerRepositoryImpl(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public Optional<Seller> findOne(String id) {
        Request request = new Request.Builder().url("").build();
        Call newCall = this.client.newCall(request);

        //FailsafeCall.with(retryPolicy).compose(newCall);

        return Optional.empty();
    }

    @Override
    public Seller save(Seller seller) {
        return null;
    }
}
