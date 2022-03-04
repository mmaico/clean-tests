package mmaico.smoothtest.sellers.view;


import au.com.origin.snapshots.annotations.SnapshotName;
import com.github.tomakehurst.wiremock.client.WireMock;
import mmaico.smoothtest.infratest.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.Arrays.asList;
import static mmaico.smoothtest.infratest.BDD.*;
import static mmaico.smoothtest.infratest.snapshot.SnapshotComparatorTest.c;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class SellerEndpointTestV1 extends BaseTest {

    private static final String SELLER_BY_ID_PAYLOAD_JSON = "{\n" +
            "          \"id\": \"53ac008b-9d16-4c36-afea-0e49c0c3515a\",\n" +
            "          \"name\": \"Boba Fett\",\n" +
            "          \"enrollment\": \"2018-12-06T17:58:16\",\n" +
            "          \"scoreId\": \"552544458\"\n" +
            "        }";

    private static final String LEVE_SELLER_PAYLOAD_JSON = "{\"value\": 5}";

    @Test
    public void shouldReturnSellerById() throws Exception {
        String sellerUri = "/sellers/53ac008b-9d16-4c36-afea-0e49c0c3515a";

        stubFor(WireMock.get(urlEqualTo(sellerUri)).willReturn(aResponse()
            .withStatus(OK.value())
            .withHeader(CONTENT_TYPE, "application/json")
            .withBody(SELLER_BY_ID_PAYLOAD_JSON)));

        String levelUri = "/sellers/53ac008b-9d16-4c36-afea-0e49c0c3515a/levels";

        stubFor(WireMock.get(urlEqualTo(levelUri)).willReturn(aResponse()
                .withStatus(OK.value())
                .withHeader(CONTENT_TYPE, "application/json")
                .withBody(LEVE_SELLER_PAYLOAD_JSON)));

        mockMvc.perform(get("/sellers/53ac008b-9d16-4c36-afea-0e49c0c3515a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("53ac008b-9d16-4c36-afea-0e49c0c3515a"))
                .andExpect(jsonPath("$.name").value("Boba Fett"))
                .andExpect(jsonPath("$.enrollment").value("2018-12-06T17:58:16.000+0000"))
                .andExpect(jsonPath("$.level").value(5))
                .andExpect(jsonPath("$._links.self.href").value("/sellers/53ac008b-9d16-4c36-afea-0e49c0c3515a"))
                .andExpect(jsonPath("$._links.hasContacts.href").value("/sellers/53ac008b-9d16-4c36-afea-0e49c0c3515a/contacts"))
                .andExpect(jsonPath("$._links.hasScore.href").value("/sellers/scores/552544458"));
    }


//    @Test
//    public void shouldFindAllSellersRegistered() throws Exception {
//        Given("a sellers api");
//        When("the api is called");
//            MvcResult response = mockMvc.perform(get("/sellers")
//                .contentType("application/hal+json")).andReturn();
//        Then("must return all sellers registered");
//            assertEquals(OK.value(), response.getResponse().getStatus());
//
//    }

//    @SnapshotName("should return seller by id using retry")
//    @Test
//    public void shouldReturnASellerUsingRetry() throws Exception {
//        Given("a seller with valid id");
//            stub("Seller search result (start)");
//            asList(2, 3, 4).forEach(count -> stub("Seller search result not finished yet (intermediate)", "count", count));
//            stub("Ended seller search result (end)");
//        And("has a velid level");
//            stub("seller level with id 68dcf440-d1fe-4ff0-ae71-9db139f77a8e");
//        When("the api is called passing a id");
//            MvcResult response = mockMvc.perform(get("/sellers/68dcf440-d1fe-4ff0-ae71-9db139f77a8e")).andReturn();
//        Then("must return the seller with all the data");
//            expect.toMatchSnapshot(response.getResponse().getContentAsString());
//            assertEquals(OK.value(), response.getResponse().getStatus());
//    }

//    @SnapshotName("should create a new seller")
//    @Test
//    public void shouldCreateANewSeller() throws Exception {
//        Given("a seller with all valid data");
//            stub("Create a new seller with all valid parameters");
//        When("the api is called passing a id");
//            MvcResult response = mockMvc.perform(post("/sellers")
//                            .contentType("application/hal+json")
//                            .content(s("seller to be created"))).andReturn();
//        Then("must return the seller with all the data");
//            assertEquals(CREATED.value(), response.getResponse().getStatus());
//            expect.comparator(c(item)).toMatchSnapshot(response.getResponse().getContentAsString());
//    }




}