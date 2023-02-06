package mmaico.clean_tests.sellers.view.support;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Date;

@Relation(collectionRelation = "sellers")
public class SellerResource extends RepresentationModel<SellerResource> {
    private String id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date enrollment;
    private int level;

    public SellerResource() {}
    public SellerResource(String id, String name, Date enrollment, int level) {
        this();
        this.id = id;
        this.name = name;
        this.enrollment = enrollment;
        this.level = level;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @JsonIgnore
    public String getScoreId() {
        return this.getRequiredLink("hasScore").getHref().replace("/sellers/scores/", "");
    }
}
