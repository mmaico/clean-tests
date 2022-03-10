package mmaico.lazytests.sellers.view;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import mmaico.lazytests.infratest.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test without any implemented feature [Maturity level 0]
 *  Statement BDD NOK
 *  Json Inside scenario file NOK
 *  Wiremock inside scenario file NOK
 *  Match Snapshot NOK
 *  Custom comparator for dynamic fields NOK
 */
class SellerEndpointTestV0 extends BaseTest {

    private static final String SELLER_BY_ID_PAYLOAD_JSON = "{\n" +
            "          \"id\": \"53ac008b-9d16-4c36-afea-0e49c0c3515a\",\n" +
            "          \"name\": \"Boba Fett\",\n" +
            "          \"enrollment\": \"2018-12-06T17:58:16\",\n" +
            "          \"scoreId\": \"552544458\"\n" +
            "        }";

    private static final String SELLER_BY_ID_PAYLOAD_JSON2 = "{\n" +
            "          \"id\": \"d580d1a0-9530-4b10-97b2-4b21de75d33a\",\n" +
            "          \"name\": \"Han Solo\",\n" +
            "          \"enrollment\": \"2018-12-06T17:58:16\",\n" +
            "          \"scoreId\": \"48847\"\n" +
            "        }";

    private static final String LEVEL_SELLER_PAYLOAD_JSON = "{\"value\": 5}";

    private static final String LEVEL_4_SELLER_PAYLOAD_JSON = "{\"value\": 4}";

    private static final String SELLERS_PAYLOAD_JSON = "[\n" +
            "          {\n" +
            "            \"id\": \"ba262eb0-5178-4a94-9771-49dd77b1c846\",\n" +
            "            \"name\": \"Baby Yoda\",\n" +
            "            \"enrollment\": \"2018-12-06T17:58:16\",\n" +
            "            \"scoreId\": \"55236\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"f6724b21-6c23-40a6-960e-233574fad5de\",\n" +
            "            \"name\": \"Jabba\",\n" +
            "            \"enrollment\": \"2018-12-06T17:58:16\",\n" +
            "            \"scoreId\": \"6464564\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"cd694802-dd85-4248-b80f-d974a2d8dbe4\",\n" +
            "            \"name\": \"Jango Fett\",\n" +
            "            \"enrollment\": \"2018-12-06T17:58:16\",\n" +
            "            \"scoreId\": \"9998887\"\n" +
            "          }\n" +
            "        ]";

    @Test
    public void shouldReturnSellerById() throws Exception {
        //Given
        String sellerUri = "/api/salesman/53ac008b-9d16-4c36-afea-0e49c0c3515a";

        stubFor(WireMock.get(urlEqualTo(sellerUri)).willReturn(aResponse()
            .withStatus(OK.value())
            .withHeader(CONTENT_TYPE, "application/json")
            .withBody(SELLER_BY_ID_PAYLOAD_JSON)));
        //AND
        String levelUri = "/api/salesman/53ac008b-9d16-4c36-afea-0e49c0c3515a/levels";

        stubFor(WireMock.get(urlEqualTo(levelUri)).willReturn(aResponse()
                .withStatus(OK.value())
                .withHeader(CONTENT_TYPE, "application/json")
                .withBody(LEVEL_SELLER_PAYLOAD_JSON)));
        //When / then
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


    @Test
    public void shouldFindAllSellersRegistered() throws Exception {
        //Given
        String sellerUri = "/api/salesman";
        stubFor(WireMock.get(urlEqualTo(sellerUri)).willReturn(aResponse()
                .withStatus(OK.value())
                .withHeader(CONTENT_TYPE, "application/json")
                .withBody(SELLERS_PAYLOAD_JSON)));
        //And
        String levelUri = "^(\\/api\\/salesman\\/(ba262eb0-5178-4a94-9771-49dd77b1c846|f6724b21-6c23-40a6-960e-233574fad5de|cd694802-dd85-4248-b80f-d974a2d8dbe4)\\/levels)$";

        stubFor(WireMock.get(urlPathMatching(levelUri)).willReturn(aResponse()
                .withStatus(OK.value())
                .withHeader(CONTENT_TYPE, "application/json")
                .withBody(LEVEL_4_SELLER_PAYLOAD_JSON)));
        //When then
        mockMvc.perform(get("/sellers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.sellers[0].id").value("ba262eb0-5178-4a94-9771-49dd77b1c846"))
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
        //Given
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
                        .withBody(SELLER_BY_ID_PAYLOAD_JSON2)));
        //And
        String levelUri = "/api/salesman/d580d1a0-9530-4b10-97b2-4b21de75d33a/levels";
        stubFor(WireMock.get(urlEqualTo(levelUri)).willReturn(aResponse()
                .withStatus(OK.value())
                .withHeader(CONTENT_TYPE, "application/json")
                .withBody(LEVEL_SELLER_PAYLOAD_JSON)));

        stubNCalls(sellerUri, HttpStatus.SERVICE_UNAVAILABLE, "{}", 4);
        //When / Then
        mockMvc.perform(get("/sellers/d580d1a0-9530-4b10-97b2-4b21de75d33a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("d580d1a0-9530-4b10-97b2-4b21de75d33a"))
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