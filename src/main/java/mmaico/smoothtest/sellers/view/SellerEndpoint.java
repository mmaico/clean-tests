package mmaico.smoothtest.sellers.view;

import mmaico.smoothtest.sellers.domain.seller.Seller;
import mmaico.smoothtest.sellers.view.support.SellerResource;
import mmaico.smoothtest.sellers.view.support.SellerResourceAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SellerEndpoint {

    private SellerResourceAssembler assembler;

    public SellerEndpoint(SellerResourceAssembler assembler) {
        this.assembler = assembler;
    }

    @GetMapping("/sellers/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SellerResource findOne(@PathVariable String id) {
        Seller result = Seller.findOne(id)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + id));

         return assembler.toModel(result);
    }

    @PostMapping ("/sellers")
    @ResponseStatus(HttpStatus.CREATED)
    public SellerResource create(@RequestBody SellerResource resource) {
        Seller seller = Seller.buildBy(resource.getName(), resource.getScoreId());
        Seller sellerSaved = seller.save();
        return assembler.toModel(sellerSaved);
    }

    @GetMapping ("/sellers")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<SellerResource> getAll() {
        return assembler.toCollectionModel(Seller.findAll());
    }
}
