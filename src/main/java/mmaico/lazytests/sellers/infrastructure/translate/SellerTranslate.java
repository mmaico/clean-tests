package mmaico.lazytests.sellers.infrastructure.translate;

import mmaico.lazytests.sellers.domain.score.Score;
import mmaico.lazytests.sellers.domain.seller.Seller;
import mmaico.lazytests.sellers.infrastructure.dao.dto.SellerDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SellerTranslate {

    public Seller translate(SellerDTO sellerDTO, int level) {
        Score score = new Score(sellerDTO.getScoreId());
        return new Seller(sellerDTO.getId(), sellerDTO.getName(), sellerDTO.getEnrollment(), score, level);
    }

    public List<Seller> translate(List<SellerDTO> list) {
        return list.stream().map( item ->  translate(item, item.getLevel())).collect(Collectors.toList());
    }
}
