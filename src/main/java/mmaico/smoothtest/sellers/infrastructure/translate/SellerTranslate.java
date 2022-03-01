package mmaico.smoothtest.sellers.infrastructure.translate;

import mmaico.smoothtest.sellers.domain.score.Score;
import mmaico.smoothtest.sellers.domain.seller.Seller;
import mmaico.smoothtest.sellers.infrastructure.dao.dto.SellerDTO;
import org.springframework.stereotype.Component;

@Component
public class SellerTranslate {

    public Seller translate(SellerDTO sellerDTO, int level) {
        Score score = new Score(sellerDTO.getScoreId());
        return new Seller(sellerDTO.getId(), sellerDTO.getName(), sellerDTO.getEnrollment(), score, level);
    }
}
