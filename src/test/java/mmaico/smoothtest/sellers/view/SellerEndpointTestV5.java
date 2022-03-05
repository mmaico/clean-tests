package mmaico.smoothtest.sellers.view;


import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.annotations.SnapshotName;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import mmaico.smoothtest.infratest.BaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static java.util.Arrays.asList;
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

/**
 * Test with Maturity level 5 (Main test with all feature implemented)
 *  Statement BDD OK
 *  Json Inside scenario file OK
 *  Wiremock inside scenario file OK
 *  Snapshot matchs OK
 *  Custom comparator for dynamic fields OK
 */
@ExtendWith({SnapshotExtension.class})
class SellerEndpointTestV5 extends BaseTest {

    private final CustomComparator item = comparators(STRICT,
            stringSize(36, "id"),
            isPresent("_links.hasContacts.href"),
            isPresent("_links.self.href"),
            isDate( "enrollment")
    );

    private final CustomComparator list = comparators(STRICT, isDate( "_embedded.sellers[0,3].enrollment"));

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

    @SnapshotName("should return seller by id using retry")
    @Test
    public void shouldReturnASellerUsingRetry() throws Exception {
        Given("a seller with valid id");
            stub("Seller search result (start)");
            asList(2, 3, 4).forEach(count -> stub("Seller search result not finished yet (intermediate)", "count", count));
            stub("Ended seller search result (end)");
        And("has a velid level");
            stub("seller level with id 68dcf440-d1fe-4ff0-ae71-9db139f77a8e");
        When("the api is called passing a id");
            MvcResult response = mockMvc.perform(get("/sellers/68dcf440-d1fe-4ff0-ae71-9db139f77a8e")).andReturn();
        Then("must return the seller with all the data");
            expect.toMatchSnapshot(response.getResponse().getContentAsString());
            assertEquals(OK.value(), response.getResponse().getStatus());
    }

    @SnapshotName("should find all sellers registered")
    @Test
    public void shouldFindAllSellersRegistered() throws Exception {
        Given("a sellers api");
            stub("find all sellers registered");
        And("have a seller level default for each request");
            stub("seller level for a list");
        When("the api is called");
            MvcResult response = mockMvc.perform(get("/sellers")
                .contentType("application/hal+json")).andReturn();
        Then("must return all sellers registered");
            assertEquals(OK.value(), response.getResponse().getStatus());
            expect.comparator(c(list)).toMatchSnapshot(response.getResponse().getContentAsString());
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
            expect.comparator(c(item)).toMatchSnapshot(response.getResponse().getContentAsString());
    }




}