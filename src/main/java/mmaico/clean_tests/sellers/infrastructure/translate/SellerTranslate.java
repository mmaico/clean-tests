package mmaico.clean_tests.sellers.infrastructure.translate;

import mmaico.clean_tests.sellers.domain.score.Score;
import mmaico.clean_tests.sellers.domain.seller.Seller;
import mmaico.clean_tests.sellers.infrastructure.dao.dto.SalesmanDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SellerTranslate {

    public Seller translate(SalesmanDTO salesmanDTO, int level) {
        Score score = new Score(salesmanDTO.getScoreId());
        return new Seller(salesmanDTO.getId(), salesmanDTO.getName(), salesmanDTO.getEnrollment(), score, level);
    }

    public List<Seller> translate(List<SalesmanDTO> list) {
        return list.stream().map( item ->  translate(item, item.getLevel())).collect(Collectors.toList());
    }
}
