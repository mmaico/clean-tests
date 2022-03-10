package mmaico.lazytests.sellers.view;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import mmaico.lazytests.infratest.BaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static mmaico.lazytests.infratest.BDD.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test with Maturity level 2
 *  Statement BDD OK
 *  Json Inside scenario file OK <----
 *  Wiremock inside scenario file NOK
 *  Match Snapshot NOK
 *  Custom comparator for dynamic fields NOK
 */
class SellerEndpointTestV2 extends BaseTest {


    @BeforeAll
    public static void beforeAll() throws IOException {
        scenarios.load("seller-scenariov3.json");
    }

    @Test
    public void shouldReturnSellerById() throws Exception {
        Given("a seller with valid id");
            String sellerUri = "/api/salesman/53ac008b-9d16-4c36-afea-0e49c0c3515a";
            stubFor(WireMock.get(urlEqualTo(sellerUri)).willReturn(aResponse()
                .withStatus(OK.value())
                .withHeader(CONTENT_TYPE, "application/json")
                .withBody(s("seller with id 53ac008b-9d16-4c36-afea-0e49c0c3515a v3"))));
        And("has a velid level");
            String levelUri = "/api/salesman/53ac008b-9d16-4c36-afea-0e49c0c3515a/levels";
            stubFor(WireMock.get(urlEqualTo(levelUri)).willReturn(aResponse()
                    .withStatus(OK.value())
                    .withHeader(CONTENT_TYPE, "application/json")
                    .withBody(s("seller level 5"))));
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
    public void shouldFindAllSellersRegistered() throws Exception {
        Given("a sellers api");
            String sellerUri = "/api/salesman";
            stubFor(WireMock.get(urlEqualTo(sellerUri)).willReturn(aResponse()
                    .withStatus(OK.value())
                    .withHeader(CONTENT_TYPE, "application/json")
                    .withBody(s("all sellers v3"))));
        And("have a seller level default for each request");
            String levelUri = "^(\\/sellers\\/(ba262eb0-5178-4a94-9771-49dd77b1c846|f6724b21-6c23-40a6-960e-233574fad5de|cd694802-dd85-4248-b80f-d974a2d8dbe4)\\/levels)$";

            stubFor(WireMock.get(urlPathMatching(levelUri)).willReturn(aResponse()
                .withStatus(OK.value())
                .withHeader(CONTENT_TYPE, "application/json")
                .withBody(s("seller level 4"))));
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

    @Test
    public void shouldReturnASellerUsingRetry() throws Exception {
        Given("a seller with valid id");
            String sellerUri = "/api/salesman/d580d1a0-9530-4b10-97b2-4b21de75d33a";
            stubFor(WireMock.get(urlEqualTo(sellerUri))
                    .inScenario("Retry")
                    .whenScenarioStateIs(Scenario.STARTED)
                    .willSetStateTo("Call-2")
                    .willReturn(aResponse()
                            .withStatus(500)
                            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .withBody("{}")));

            stubFor(WireMock.get(urlEqualTo(sellerUri))
                    .inScenario("Retry")
                    .whenScenarioStateIs("Call-5")
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .withBody(s("seller with id d580d1a0-9530-4b10-97b2-4b21de75d33a v3"))));
            stubNCalls(sellerUri, HttpStatus.SERVICE_UNAVAILABLE, "{}", 4);
        And("has a velid level");
            String levelUri = "/api/salesman/d580d1a0-9530-4b10-97b2-4b21de75d33a/levels";
            stubFor(WireMock.get(urlEqualTo(levelUri)).willReturn(aResponse()
                .withStatus(OK.value())
                .withHeader(CONTENT_TYPE, "application/json")
                .withBody(s("seller level 5"))));

        When("the api is called passing a id");
            ResultActions resultActions = mockMvc.perform(get("/sellers/d580d1a0-9530-4b10-97b2-4b21de75d33a"))
                .andExpect(status().isOk());
        Then("must return the seller with all the data");
            resultActions.andExpect(jsonPath("$.id").value("d580d1a0-9530-4b10-97b2-4b21de75d33a"))
                .andExpect(jsonPath("$.name").value("Han Solo"))
                .andExpect(jsonPath("$.enrollment").value("2018-12-06T17:58:16.000+0000"))
                .andExpect(jsonPath("$.level").value(5))
                .andExpect(jsonPath("$._links.self.href").value("/sellers/d580d1a0-9530-4b10-97b2-4b21de75d33a"))
                .andExpect(jsonPath("$._links.hasContacts.href").value("/sellers/d580d1a0-9530-4b10-97b2-4b21de75d33a/contacts"))
                .andExpect(jsonPath("$._links.hasScore.href").value("/sellers/scores/48847"));

    }


    private void stubNCalls(String url, HttpStatus status, String response, int times) {
        for (int i = 2; i <= times; i++)
            stubFor(WireMock.get(urlEqualTo(url))
                    .inScenario("Retry")
                    .whenScenarioStateIs("Call-" + i)
                    .willSetStateTo("Call-" + (i + 1))
                    .willReturn(aResponse()
                            .withStatus(status.value())
                            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .withBody(response)));
    }




}