package mmaico.smoothtest.infratest;

import mmaico.smoothtest.infratest.scenarios.ScenarioLoaderHelper;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseTest {

    protected ScenarioLoaderHelper scenarios = new ScenarioLoaderHelper();
    private static WireMockServer server;

    static {
        final int PORT = 8181;
        server = new WireMockServer(WireMockConfiguration.wireMockConfig().port(PORT));
        server.start();
        WireMock.configureFor("localhost", PORT);
    }

    protected String s(String scenarioName) {
        return this.scenario.getScenario(scenarioName).getJson();
    }

    protected void stub(String scenario) {
        String jsonResult = this.scenarios.getScenario(scenario).getJson();
        server.addStubMapping(StubMapping.buildFrom(jsonResult));
    }


}
