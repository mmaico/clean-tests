package mmaico.smoothtest.sellers.view;


import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.annotations.SnapshotName;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import mmaico.smoothtest.infratest.BaseTest;
import mmaico.smoothtest.infratest.comparators.Comparators;
import mmaico.smoothtest.infratest.comparators.IsPresentExpressionMatcher;
import mmaico.smoothtest.infratest.comparators.StringSizeExpressionMatcher;
import mmaico.smoothtest.infratest.snapshot.SnapshotComparatorTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static mmaico.smoothtest.infratest.BDD.*;
import static mmaico.smoothtest.infratest.comparators.Comparators.comparators;
import static mmaico.smoothtest.infratest.comparators.DateExpressionMatcher.isDate;
import static mmaico.smoothtest.infratest.comparators.IsPresentExpressionMatcher.isPresent;
import static mmaico.smoothtest.infratest.comparators.StringSizeExpressionMatcher.stringSize;
import static mmaico.smoothtest.infratest.snapshot.SnapshotComparatorTest.c;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith({SnapshotExtension.class})
class SellerEndpointTest extends BaseTest {

    private final CustomComparator comparator = comparators(STRICT,
            stringSize(36, "id"),
            isPresent("_links.hasContacts.href"),
            isPresent("_links.self.href"),
            isDate( "enrollment")
    );

    private Expect expect;


    @BeforeAll
    public static void beforeAll() throws IOException {
        scenarios.load("seller-scenario.json");
    }

    @BeforeEach
    public void setUp() {
        this.expect = expect.serializer("json");
    }

    @SnapshotName("should return seller by id")
    @Test
    public void shouldReturnSellerById() throws Exception {
        Given("a seller with valid id");
            stub("seller with id 53ac008b-9d16-4c36-afea-0e49c0c3515a");
        And("has a velid level");
            stub("seller level with id 53ac008b-9d16-4c36-afea-0e49c0c3515a");
        When("the api is called passing a id");
            MvcResult response = mockMvc.perform(get("/sellers/53ac008b-9d16-4c36-afea-0e49c0c3515a")).andReturn();
        Then("must return the seller with all the data");
            expect.toMatchSnapshot(response.getResponse().getContentAsString());
            assertEquals(OK.value(), response.getResponse().getStatus());
    }

    @SnapshotName("should create a new seller")
    @Test
    public void shouldCreateANewSeller() throws Exception {
        Given("a seller with all valid data");
            stub("Create a new seller with all valid parameters");
        When("the api is called passing a id");
            MvcResult response = mockMvc.perform(post("/sellers")
                            .contentType("application/hal+json")
                            .content(s("seller to be created"))).andReturn();
        Then("must return the seller with all the data");
            assertEquals(CREATED.value(), response.getResponse().getStatus());
            expect.comparator(c(comparator)).toMatchSnapshot(response.getResponse().getContentAsString());
    }


}