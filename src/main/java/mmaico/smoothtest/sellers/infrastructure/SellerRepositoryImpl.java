package mmaico.smoothtest.sellers.infrastructure;

import mmaico.smoothtest.sellers.domain.score.Score;
import mmaico.smoothtest.sellers.domain.seller.Seller;
import mmaico.smoothtest.sellers.domain.seller.SellerRepository;
import mmaico.smoothtest.sellers.infrastructure.dao.LevelDAO;
import mmaico.smoothtest.sellers.infrastructure.dao.SellerDAO;
import mmaico.smoothtest.sellers.infrastructure.dao.dto.SellerDTO;
import mmaico.smoothtest.sellers.infrastructure.translate.SellerTranslate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Arrays.asList;

@Repository
public class SellerRepositoryImpl implements SellerRepository {

    private static final int LEVEL_DEFAULT = 5;
    private SellerDAO sellerDAO;
    private LevelDAO levelDAO;
    private SellerTranslate translate;

    public SellerRepositoryImpl(SellerDAO sellerDAO, LevelDAO levelDAO, SellerTranslate translate) {
        this.sellerDAO = sellerDAO;
        this.levelDAO = levelDAO;
        this.translate = translate;
    }

    @Override
    public Optional<Seller> findOne(String id) {
        Optional<SellerDTO> seller = sellerDAO.getSeller(id);
        int level = levelDAO.getLevelBy(id);

        if (!seller.isPresent()) return Optional.empty();

        return Optional.of(translate.translate(seller.get(), level));
    }

    @Override
    public Seller save(Seller seller) {
        SellerDTO dto = new SellerDTO();
        dto.setName(seller.getName());
        dto.setScoreId(seller.getScore().getId());

        SellerDTO result = sellerDAO.createSeller(dto)
                .orElseThrow(() -> new RuntimeException("Error when create"));

        return translate.translate(result, LEVEL_DEFAULT);
    }

    @Override
    public List<Seller> findAll() {
        Seller babyYoda = new Seller("ba262eb0-5178-4a94-9771-49dd77b1c846", "Baby Yoda", new Date(), new Score("55236"), 5);
        Seller jabba = new Seller("f6724b21-6c23-40a6-960e-233574fad5de", "Jabba", new Date(), new Score("6464564"), 4);
        Seller jango = new Seller("cd694802-dd85-4248-b80f-d974a2d8dbe4", "Jango Fett", new Date(), new Score("9998887"), 8);
        return asList(babyYoda, jabba, jango);
    }
}
