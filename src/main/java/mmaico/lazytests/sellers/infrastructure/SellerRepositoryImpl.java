package mmaico.lazytests.sellers.infrastructure;

import mmaico.lazytests.sellers.domain.seller.Seller;
import mmaico.lazytests.sellers.domain.seller.SellerRepository;
import mmaico.lazytests.sellers.infrastructure.dao.LevelDAO;
import mmaico.lazytests.sellers.infrastructure.dao.SellerDAO;
import mmaico.lazytests.sellers.infrastructure.dao.dto.SellerDTO;
import mmaico.lazytests.sellers.infrastructure.translate.SellerTranslate;
import org.springframework.stereotype.Repository;

import java.util.*;

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
        List<SellerDTO> all = sellerDAO.getAll();
        all.parallelStream().forEach(dto -> dto.setLevel(levelDAO.getLevelBy(dto.getId())));

        return translate.translate(all);
    }
}
