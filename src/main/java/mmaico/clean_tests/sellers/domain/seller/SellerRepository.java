package mmaico.clean_tests.sellers.domain.seller;

import java.util.List;
import java.util.Optional;

public interface SellerRepository {

    Optional<Seller> findOne(String id);
    Seller save(Seller seller);
    
    List<Seller> findAll();
}
