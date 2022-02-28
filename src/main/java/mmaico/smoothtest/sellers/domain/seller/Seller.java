package mmaico.smoothtest.sellers.domain.seller;

import mmaico.smoothtest.sellers.domain.score.Score;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static mmaico.smoothtest.infrastructure.ServiceLocator.getBean;

public class Seller {

    private final String id;
    private final String name;
    private final Date enrollment;
    private final Score score;
    private final SellerRepository repository;

    public Seller(String id, String name, Date enrollment, Score contact) {
        this.id = id;
        this.name = name;
        this.enrollment = enrollment;
        this.score = contact;
        this.repository = getBean(SellerRepository.class);
    }

    public Seller(String id, String name) {
        this(id, name, new Date(), new Score(""));
    }

    public static Optional<Seller> findOne(String id) {
        return getBean(SellerRepository.class).findOne(id);
    }
    public static Seller buildBy(String name, String scoreId) {
        return new Seller(UUID.randomUUID().toString(), name, new Date(), new Score(scoreId));
    }
    public Seller save() {
        return repository.save(this);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getEnrollment() {
        return enrollment;
    }

    public Score getScore() {
        return score;
    }
}
