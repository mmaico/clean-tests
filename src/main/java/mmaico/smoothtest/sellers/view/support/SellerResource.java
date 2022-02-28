package mmaico.smoothtest.sellers.view.support;

import net.minidev.json.annotate.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Date;

@Relation(collectionRelation = "sellers")
public class SellerResource extends RepresentationModel<SellerResource> {
    private String id;
    private String name;
    private Date enrollment;

    public SellerResource() {}
    public SellerResource(String id, String name, Date enrollment) {
        this();
        this.id = id;
        this.name = name;
        this.enrollment = enrollment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Date enrollment) {
        this.enrollment = enrollment;
    }

    @JsonIgnore
    public String getScoreId() {
        return this.getRequiredLink("score").getHref().replace("/sellers/scores/", "");
    }
}
