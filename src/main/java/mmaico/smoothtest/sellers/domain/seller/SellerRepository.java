package mmaico.smoothtest.sellers.domain.seller;

import java.util.Optional;

public interface SellerRepository {

    Optional<Seller> findOne(String id);
    Seller save(Seller seller);
}
