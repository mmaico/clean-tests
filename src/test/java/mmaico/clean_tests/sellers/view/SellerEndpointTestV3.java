package mmaico.clean_tests.sellers.view;


import mmaico.clean_tests.infratest.BaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;

import static java.util.Arrays.asList;
import static mmaico.clean_tests.infratest.BDD.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test with Maturity level 3
 *  Statement BDD OK
 *  Json Inside scenario file OK
 *  Wiremock inside scenario file OK <----
 *  Match Snapshot NOK
 *  Custom comparator for dynamic fields NOK
 */
class SellerEndpointTestV3 extends BaseTest {


    @BeforeAll
    public static void beforeAll() throws IOException {
        scenarios.load("seller-scenario.json");
    }

    @Test
    public void shouldReturnSellerById() throws Exception {
        Given("a seller with valid id");
            stub("seller with id 53ac008b-9d16-4c36-afea-0e49c0c3515a");
        And("has a velid level");
            stub("seller level with id 53ac008b-9d16-4c36-afea-0e49c0c3515a");
        When("the api is called passing a id");
            ResultActions perform = mockMvc.perform(get("/sellers/53ac008b-9d16-4c36-afea-0e49c0c3515a"));

        Then("must return the seller with all the data");
            perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("53ac008b-9d16-4c36-afea-0e49c0c3515a"))
                .andExpect(jsonPath("$.name").value("Boba Fett"))
                .andExpect(jsonPath("$.enrollment").value("2018-12-06T17:58:16.000+0000"))
                .andExpect(jsonPath("$.level").value(5))
                .andExpect(jsonPath("$._links.self.href").value("/sellers/53ac008b-9d16-4c36-afea-0e49c0c3515a"))
                .andExpect(jsonPath("$._links.hasContacts.href").value("/sellers/53ac008b-9d16-4c36-afea-0e49c0c3515a/contacts"))
                .andExpect(jsonPath("$._links.hasScore.href").value("/sellers/scores/552544458"));
    }

    @Test
    public void shouldReturnASellerUsingRetry() throws Exception {
        Given("a seller with valid id");
            stub("Seller search result (start)");
            asList(2, 3, 4).forEach(count -> stub("Seller search result not finished yet (intermediate)", "count", count));
            stub("Ended seller search result (end)");
        And("has a velid level");
            stub("seller level with id 68dcf440-d1fe-4ff0-ae71-9db139f77a8e");
        When("the api is called passing a id");
            ResultActions resultActions = mockMvc.perform(get("/sellers/68dcf440-d1fe-4ff0-ae71-9db139f77a8e"))
                .andExpect(status().isOk());
        Then("must return the seller with all the data");
            resultActions.andExpect(jsonPath("$.id").value("68dcf440-d1fe-4ff0-ae71-9db139f77a8e"))
                .andExpect(jsonPath("$.name").value("Chewbacca"))
                .andExpect(jsonPath("$.enrollment").value("2022-12-06T17:58:16.000+0000"))
                .andExpect(jsonPath("$.level").value(9))
                .andExpect(jsonPath("$._links.self.href").value("/sellers/68dcf440-d1fe-4ff0-ae71-9db139f77a8e"))
                .andExpect(jsonPath("$._links.hasContacts.href").value("/sellers/68dcf440-d1fe-4ff0-ae71-9db139f77a8e/contacts"))
                .andExpect(jsonPath("$._links.hasScore.href").value("/sellers/scores/9995562325"));

    }

    @Test
    public void shouldFindAllSellersRegistered() throws Exception {
        Given("a sellers api");
            stub("find all sellers registered");
        And("have a seller level default for each request");
            stub("seller level for a list");
        When("the api is called");
            ResultActions resultActions = mockMvc.perform(get("/sellers"))
                .andExpect(status().isOk());
        Then("must return all sellers registered");
            resultActions.andExpect(jsonPath("$._embedded.sellers[0].id").value("ba262eb0-5178-4a94-9771-49dd77b1c846"))
                .andExpect(jsonPath("$._embedded.sellers[0].name").value("Baby Yoda"))
                .andExpect(jsonPath("$._embedded.sellers[0].enrollment").value("2018-12-06T17:58:16.000+0000"))
                .andExpect(jsonPath("$._embedded.sellers[0].level").value(4))
                .andExpect(jsonPath("$._embedded.sellers[0]._links.self.href").value("/sellers/ba262eb0-5178-4a94-9771-49dd77b1c846"))
                .andExpect(jsonPath("$._embedded.sellers[0]._links.hasContacts.href").value("/sellers/ba262eb0-5178-4a94-9771-49dd77b1c846/contacts"))
                .andExpect(jsonPath("$._embedded.sellers[0]._links.hasScore.href").value("/sellers/scores/55236"))

                .andExpect(jsonPath("$._embedded.sellers[1].id").value("f6724b21-6c23-40a6-960e-233574fad5de"))
                .andExpect(jsonPath("$._embedded.sellers[1].name").value("Jabba"))
                .andExpect(jsonPath("$._embedded.sellers[1].enrollment").value("2018-12-06T17:58:16.000+0000"))
                .andExpect(jsonPath("$._embedded.sellers[1].level").value(4))
                .andExpect(jsonPath("$._embedded.sellers[1]._links.self.href").value("/sellers/f6724b21-6c23-40a6-960e-233574fad5de"))
                .andExpect(jsonPath("$._embedded.sellers[1]._links.hasContacts.href").value("/sellers/f6724b21-6c23-40a6-960e-233574fad5de/contacts"))
                .andExpect(jsonPath("$._embedded.sellers[1]._links.hasScore.href").value("/sellers/scores/6464564"))

                .andExpect(jsonPath("$._embedded.sellers[2].id").value("cd694802-dd85-4248-b80f-d974a2d8dbe4"))
                .andExpect(jsonPath("$._embedded.sellers[2].name").value("Jango Fett"))
                .andExpect(jsonPath("$._embedded.sellers[2].enrollment").value("2018-12-06T17:58:16.000+0000"))
                .andExpect(jsonPath("$._embedded.sellers[2].level").value(4))
                .andExpect(jsonPath("$._embedded.sellers[2]._links.self.href").value("/sellers/cd694802-dd85-4248-b80f-d974a2d8dbe4"))
                .andExpect(jsonPath("$._embedded.sellers[2]._links.hasContacts.href").value("/sellers/cd694802-dd85-4248-b80f-d974a2d8dbe4/contacts"))
                .andExpect(jsonPath("$._embedded.sellers[2]._links.hasScore.href").value("/sellers/scores/9998887"));
    }

}